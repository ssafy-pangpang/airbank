package com.pangpang.airbank.domain.group.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.fund.domain.FundManagement;
import com.pangpang.airbank.domain.fund.repository.FundManagementRepository;
import com.pangpang.airbank.domain.group.domain.MemberRelationship;
import com.pangpang.airbank.domain.group.dto.CommonFundManagementRequestDto;
import com.pangpang.airbank.domain.group.dto.CommonIdResponseDto;
import com.pangpang.airbank.domain.group.dto.GetPartnersResponseDto;
import com.pangpang.airbank.domain.group.dto.PatchConfirmRequestDto;
import com.pangpang.airbank.domain.group.dto.PatchFundManagementResponseDto;
import com.pangpang.airbank.domain.group.dto.PostEnrollChildRequestDto;
import com.pangpang.airbank.domain.group.repository.MemberRelationshipRepository;
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
	private final MemberRelationshipRepository memberRelationshipRepository;
	private final MemberRepository memberRepository;
	private final FundManagementRepository fundManagementRepository;

	@Transactional(readOnly = true)
	@Override
	public GetPartnersResponseDto getPartners(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		List<MemberRelationship> memberRelationships = new ArrayList<>();

		if (member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			memberRelationships = memberRelationshipRepository.findAllByParentIdWithChildAsActive(member.getId());
		}

		if (member.getRole().getName().equals(MemberRole.CHILD.getName())) {
			memberRelationships = memberRelationshipRepository.findAllByChildIdWithParentAsActive(member.getId());
		}

		return GetPartnersResponseDto.of(memberRelationships, member);
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

		memberRelationshipRepository.findByChildId(childMember.getId()).ifPresent((memberRelationship) -> {
			if (memberRelationship.getActivated()) {
				throw new GroupException(GroupErrorInfo.ALREADY_HAD_PARENT);
			}
			throw new GroupException(GroupErrorInfo.ENROLL_IN_PROGRESS);
		});

		MemberRelationship memberRelationship = MemberRelationship.of(member, childMember);
		return new CommonIdResponseDto(memberRelationshipRepository.save(memberRelationship).getId());
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

		MemberRelationship memberRelationship = memberRelationshipRepository.findByIdAndChildId(groupId, member.getId())
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_MEMBER_RELATIONSHIP_BY_CHILD_ID));

		if (patchConfirmRequestDto.getIsAccept()) {
			memberRelationship.setActivated(true);
			return new CommonIdResponseDto(memberRelationship.getId());
		}

		memberRelationship.setActivated(false);
		return new CommonIdResponseDto(memberRelationship.getId());
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

		MemberRelationship memberRelationship = memberRelationshipRepository.findByIdAndParentId(groupId,
				member.getId())
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_MEMBER_RELATIONSHIP_BY_PARENT_ID));

		if (fundManagementRepository.existsByMemberRelationshipId(memberRelationship.getId())) {
			throw new FundException(FundErrorInfo.ALREADY_EXISTS_FUND_MANAGEMENT);
		}

		FundManagement fundManagement = FundManagement.of(memberRelationship, commonFundManagementRequestDto);
		return new CommonIdResponseDto(fundManagementRepository.save(fundManagement).getId());
	}

	@Transactional
	@Override
	public PatchFundManagementResponseDto updateFundManagement(Long memberId,
		CommonFundManagementRequestDto commonFundManagementRequestDto, Long groupId) {

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		if (!member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			throw new FundException(FundErrorInfo.UPDATE_FUND_MANAGEMENT_PERMISSION_DENIED);
		}

		MemberRelationship memberRelationship = memberRelationshipRepository.findByIdAndParentId(groupId,
				member.getId())
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_MEMBER_RELATIONSHIP_BY_PARENT_ID));

		FundManagement fundManagement = fundManagementRepository.findByMemberRelationshipId(memberRelationship.getId())
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_MEMBER_RELATIONSHIP_ID));

		fundManagement.updateFundManagement(commonFundManagementRequestDto);
		return PatchFundManagementResponseDto.from(commonFundManagementRequestDto);
	}

}
