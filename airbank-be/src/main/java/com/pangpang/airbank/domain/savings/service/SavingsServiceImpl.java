package com.pangpang.airbank.domain.savings.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.group.repository.GroupRepository;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.domain.savings.domain.Savings;
import com.pangpang.airbank.domain.savings.domain.SavingsItem;
import com.pangpang.airbank.domain.savings.dto.GetCurrentSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchCancelSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PatchCommonSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchConfirmSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostSaveSavingsRequestDto;
import com.pangpang.airbank.domain.savings.repository.SavingsItemRepository;
import com.pangpang.airbank.domain.savings.repository.SavingsRepository;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.SavingsException;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.SavingsErrorInfo;
import com.pangpang.airbank.global.meta.domain.MemberRole;
import com.pangpang.airbank.global.meta.domain.SavingsStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingsServiceImpl implements SavingsService {
	private final SavingsRepository savingsRepository;
	private final SavingsItemRepository savingsItemRepository;
	private final GroupRepository groupRepository;
	private final MemberRepository memberRepository;

	/**
	 *  현재 진행중인 티끌모으기 정보를 조회하는 메소드
	 *
	 * @param groupId Long
	 * @return GetCurrentSavingsResponseDto
	 * @see SavingsRepository
	 * @see SavingsItemRepository
	 */
	@Transactional(readOnly = true)
	@Override
	public GetCurrentSavingsResponseDto getCurrentSavings(Long groupId) {
		Savings savings = savingsRepository.findByGroupIdAndStatusEquals(groupId, SavingsStatus.PROCEEDING)
			.orElseThrow(() -> new SavingsException(SavingsErrorInfo.NOT_FOUND_SAVINGS_IN_PROCEEDING));

		SavingsItem savingsItem = savingsItemRepository.findBySavings(savings)
			.orElseThrow(() -> new SavingsException(SavingsErrorInfo.NOT_FOUND_SAVINGS_ITEM));
		return GetCurrentSavingsResponseDto.of(savings, savingsItem);
	}

	/**
	 *  티끌모으기를 생성하는 메소드, 자녀만 생성할 수 있음.
	 *
	 * @param memberId Long
	 * @param postSaveSavingsRequestDto PostSaveSavingsRequestDto
	 * @return CommonIdResponseDto
	 * @see MemberRepository
	 * @see GroupRepository
	 * @see SavingsRepository
	 * @see SavingsItemRepository
	 */
	@Transactional
	@Override
	public CommonIdResponseDto saveSavings(Long memberId, PostSaveSavingsRequestDto postSaveSavingsRequestDto) {
		if (!memberRepository.existsByIdAndRoleEquals(memberId, MemberRole.CHILD)) {
			throw new SavingsException(SavingsErrorInfo.ENROLL_SAVINGS_PERMISSION_DENIED);
		}

		Group group = groupRepository.findByChildIdAndActivatedTrue(memberId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_CHILD));

		if (savingsRepository.existsByGroupIdAndStatusEquals(group.getId(), SavingsStatus.PENDING)) {
			throw new SavingsException(SavingsErrorInfo.ALREADY_SAVINGS_IN_PENDING);
		}

		if (savingsRepository.existsByGroupIdAndStatusEquals(group.getId(), SavingsStatus.PROCEEDING)) {
			throw new SavingsException(SavingsErrorInfo.ALREADY_SAVINGS_IN_PROCEEDING);
		}

		Savings savings = Savings.of(group, postSaveSavingsRequestDto);
		SavingsItem savingsItem = SavingsItem.of(savings, postSaveSavingsRequestDto);

		savingsRepository.save(savings);
		savingsItemRepository.save(savingsItem);
		return CommonIdResponseDto.from(savings.getId());
	}

	/**
	 *  티끌모으기 요청을 수락 또는 거절하는 메소드, 부모만 가능.
	 * @param memberId Long
	 * @param patchConfirmSavingsRequestDto PatchConfirmSavingsRequestDto
	 * @param groupId Long
	 * @return PatchConfirmSavingsResponseDto
	 * @see SavingsRepository
	 */
	@Transactional
	@Override
	public PatchCommonSavingsResponseDto confirmEnrollmentSavings(
		Long memberId, PatchConfirmSavingsRequestDto patchConfirmSavingsRequestDto, Long groupId) {
		if (!memberRepository.existsByIdAndRoleEquals(memberId, MemberRole.PARENT)) {
			throw new SavingsException(SavingsErrorInfo.CONFIRM_SAVINGS_PERMISSION_DENIE);
		}

		Savings savings = savingsRepository.findByGroupIdAndStatusEquals(groupId, SavingsStatus.PENDING)
			.orElseThrow(() -> new SavingsException(SavingsErrorInfo.NOT_FOUND_SAVINGS_IN_PENDING));

		savings.confirmSavings(patchConfirmSavingsRequestDto);
		return PatchCommonSavingsResponseDto.from(savings);
	}

	/**
	 *  진행중인 티끌모으기를 포기하는 메소드
	 *
	 * @param memberId Long
	 * @param patchCancelSavingsRequestDto PatchCancelSavingsRequestDto
	 * @return PatchCommonSavingsResponseDto
	 * @see MemberRepository
	 * @see SavingsRepository
	 */
	@Transactional
	@Override
	public PatchCommonSavingsResponseDto cancelSavings(Long memberId,
		PatchCancelSavingsRequestDto patchCancelSavingsRequestDto) {

		if (!memberRepository.existsByIdAndRoleEquals(memberId, MemberRole.CHILD)) {
			throw new SavingsException(SavingsErrorInfo.CANCEL_SAVINGS_PERMISSION_DENIED);
		}

		Savings savings = savingsRepository.findById(patchCancelSavingsRequestDto.getId())
			.orElseThrow(() -> new SavingsException(SavingsErrorInfo.NOT_FOUND_SAVINGS_IN_PROCEEDING));

		if (savings.getStatus().getName().equals(SavingsStatus.FAIL.getName())) {
			throw new SavingsException(SavingsErrorInfo.ALREADY_EXIT_SAVINGS);
		}

		savings.cancelSavings();
		return PatchCommonSavingsResponseDto.from(savings);
	}
}
