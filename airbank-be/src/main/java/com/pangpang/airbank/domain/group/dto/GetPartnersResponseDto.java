package com.pangpang.airbank.domain.group.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.pangpang.airbank.domain.group.domain.MemberRelationship;
import com.pangpang.airbank.domain.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPartnersResponseDto {
	private List<PartnerElement> members;

	public static GetPartnersResponseDto of(List<MemberRelationship> memberRelationships, Member member) {
		return GetPartnersResponseDto.builder()
			.members(memberRelationships.stream()
				.map(memberRelationship -> PartnerElement.of(memberRelationship,
					memberRelationship.getPartnerMember(member)))
				.collect(Collectors.toList()))
			.build();
	}
}
