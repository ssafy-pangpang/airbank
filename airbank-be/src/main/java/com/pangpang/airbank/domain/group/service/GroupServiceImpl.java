package com.pangpang.airbank.domain.group.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.group.domain.MemberRelationship;
import com.pangpang.airbank.domain.group.dto.GetPartnersResponseDto;
import com.pangpang.airbank.domain.group.dto.PatchConfirmRequestDto;
import com.pangpang.airbank.domain.group.dto.PostEnrollChildRequestDto;
import com.pangpang.airbank.domain.group.repository.MemberRelationshipRepository;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.meta.domain.MemberRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
	private final MemberRelationshipRepository memberRelationshipRepository;
	private final MemberRepository memberRepository;

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
	public Long enrollChild(Long memberId, PostEnrollChildRequestDto postEnrollChildRequestDto) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		if (!member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			throw new GroupException(GroupErrorInfo.ENROLL_PERMISSION_DENIED);
		}

		Member childMember = memberRepository.findByChildPhoneNumber(postEnrollChildRequestDto.getPhoneNumber())
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_CHILD_MEMBER_BY_PHONE_NUMBER));

		memberRelationshipRepository.findByChildId(childMember.getId())
			.ifPresent(
				(memberRelationship) -> {
					if (memberRelationship.getActivated()) {
						throw new GroupException(GroupErrorInfo.ALREADY_HAD_PARENT);
					}
					throw new GroupException(GroupErrorInfo.ENROLL_IN_PROGRESS);
				}
			);

		MemberRelationship memberRelationship = MemberRelationship.of(member, childMember);
		return memberRelationshipRepository.save(memberRelationship).getId();
	}

	@Transactional
	@Override
	public Long confirmEnrollment(Long memberId, PatchConfirmRequestDto patchConfirmRequestDto, Long groupId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		if (!member.getRole().getName().equals(MemberRole.CHILD.getName())) {
			throw new GroupException(GroupErrorInfo.CONFIRM_PERMISSION_DENIED);
		}

		MemberRelationship memberRelationship = memberRelationshipRepository.findByIdAndChildId(groupId, member.getId())
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_MEMBER_RELATIONSHIP_BY_CHILD));

		if (patchConfirmRequestDto.getIsAccept()) {
			memberRelationship.setActivated(true);
			return memberRelationship.getId();
		}

		memberRelationship.setActivated(false);
		return memberRelationship.getId();
	}
}
