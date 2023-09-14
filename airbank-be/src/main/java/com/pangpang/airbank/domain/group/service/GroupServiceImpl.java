package com.pangpang.airbank.domain.group.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.group.domain.MemberRelationship;
import com.pangpang.airbank.domain.group.dto.GetPartnersResponseDto;
import com.pangpang.airbank.domain.group.repository.MemberRelationshipRepository;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.meta.domain.MemberRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
	private final MemberRelationshipRepository memberRelationshipRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public GetPartnersResponseDto getPartners(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		List<MemberRelationship> memberRelationships = new ArrayList<>();

		if (member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			memberRelationships = memberRelationshipRepository.findAllByParentIdWithChild(member.getId());
		}

		if (member.getRole().getName().equals(MemberRole.CHILD.getName())) {
			memberRelationships = memberRelationshipRepository.findAllByChildIdWithParent(member.getId());
		}

		return GetPartnersResponseDto.of(memberRelationships, member);
	}
}
