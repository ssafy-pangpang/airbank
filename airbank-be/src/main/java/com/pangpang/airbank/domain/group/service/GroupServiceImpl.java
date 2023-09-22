package com.pangpang.airbank.domain.group.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.fund.domain.FundManagement;
import com.pangpang.airbank.domain.fund.repository.FundManagementRepository;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.group.dto.CommonFundManagementRequestDto;
import com.pangpang.airbank.domain.group.dto.CommonIdResponseDto;
import com.pangpang.airbank.domain.group.dto.GetPartnersResponseDto;
import com.pangpang.airbank.domain.group.dto.PatchConfirmRequestDto;
import com.pangpang.airbank.domain.group.dto.PatchFundManagementResponseDto;
import com.pangpang.airbank.domain.group.dto.PostEnrollChildRequestDto;
import com.pangpang.airbank.domain.group.repository.GroupRepository;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.error.exception.FundException;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.FundErrorInfo;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.meta.domain.MemberRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl implements GroupService {
	private final GroupRepository groupRepository;
	private final MemberRepository memberRepository;
	private final FundManagementRepository fundManagementRepository;

	@Transactional(readOnly = true)
	@Override
	public GetPartnersResponseDto getPartners(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		List<Group> groups = new ArrayList<>();

		if (member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			groups = groupRepository.findAllByParentIdWithChildAsActive(member.getId());
		}

		if (member.getRole().getName().equals(MemberRole.CHILD.getName())) {
			groups = groupRepository.findAllByChildIdWithParentAsActive(member.getId());
		}

		return GetPartnersResponseDto.of(groups, memberId);
	}

	@Transactional
	@Override
	public CommonIdResponseDto enrollChild(Long memberId, PostEnrollChildRequestDto postEnrollChildRequestDto) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		if (!member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			throw new GroupException(GroupErrorInfo.ENROLL_PERMISSION_DENIED);
		}

		Member childMember = memberRepository.findByChildPhoneNumber(postEnrollChildRequestDto.getPhoneNumber())
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_CHILD_MEMBER_BY_PHONE_NUMBER));

		groupRepository.findByChildId(childMember.getId()).ifPresent((group) -> {
			if (group.getActivated()) {
				throw new GroupException(GroupErrorInfo.ALREADY_HAD_PARENT);
			}
			throw new GroupException(GroupErrorInfo.ENROLL_IN_PROGRESS);
		});

		Group group = Group.of(member, childMember);
		return new CommonIdResponseDto(groupRepository.save(group).getId());
	}

	@Transactional
	@Override
	public CommonIdResponseDto confirmEnrollment(Long memberId, PatchConfirmRequestDto patchConfirmRequestDto,
		Long groupId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		if (!member.getRole().getName().equals(MemberRole.CHILD.getName())) {
			throw new GroupException(GroupErrorInfo.CONFIRM_PERMISSION_DENIED);
		}

		Group group = groupRepository.findByIdAndChildId(groupId, member.getId())
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_CHILD_ID));

		if (patchConfirmRequestDto.getIsAccept()) {
			group.setActivated(true);
			return new CommonIdResponseDto(group.getId());
		}

		group.setActivated(false);
		return new CommonIdResponseDto(group.getId());
	}

	@Transactional
	@Override
	public CommonIdResponseDto saveFundManagement(Long memberId,
		CommonFundManagementRequestDto commonFundManagementRequestDto, Long groupId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		if (!member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			throw new FundException(FundErrorInfo.UPDATE_FUND_MANAGEMENT_PERMISSION_DENIED);
		}

		Group group = groupRepository.findByIdAndParentId(groupId,
				member.getId())
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_PARENT_ID));

		if (fundManagementRepository.existsByGroupId(group.getId())) {
			throw new FundException(FundErrorInfo.ALREADY_EXISTS_FUND_MANAGEMENT);
		}

		FundManagement fundManagement = FundManagement.of(group, commonFundManagementRequestDto);
		return new CommonIdResponseDto(fundManagementRepository.save(fundManagement).getId());
	}

	/**
	 *  자금 관리 수정
	 *
	 * @param memberId Long
	 * @param commonFundManagementRequestDto CommonFundManagementRequestDto
	 * @param groupId Long
	 * @return PatchFundManagementResponseDto
	 * @see MemberRepository
	 * @see GroupRepository
	 * @see FundManagementRepository
	 */
	@Transactional
	@Override
	public PatchFundManagementResponseDto updateFundManagement(Long memberId,
		CommonFundManagementRequestDto commonFundManagementRequestDto, Long groupId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		if (!member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			throw new FundException(FundErrorInfo.UPDATE_FUND_MANAGEMENT_PERMISSION_DENIED);
		}

		log.info(String.valueOf(groupId));
		log.info(String.valueOf(member.getId()));
		Group group = groupRepository.findByIdAndParentId(groupId,
				member.getId())
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_PARENT_ID));

		FundManagement fundManagement = fundManagementRepository.findByGroupId(group.getId())
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP_ID));

		fundManagement.updateFundManagement(commonFundManagementRequestDto);
		return PatchFundManagementResponseDto.from(commonFundManagementRequestDto);
	}

	/**
	 *  memberId와 groupId가 매치되어 유효한 그룹인지 확인
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return Boolean
	 * @see MemberRepository
	 */
	@Transactional(readOnly = true)
	@Override
	public Boolean isMemberInGroup(Long memberId, Long groupId) {
		return groupRepository.existsByIdAndPartnerId(groupId, memberId);
	}

}
