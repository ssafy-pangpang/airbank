package com.pangpang.airbank.domain.group.dto;

import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.member.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerElement {
	private Long id;
	private Long groupId;
	private String name;
	private String imageUrl;
	private Integer creditScore;

	public static PartnerElement of(Group group, Member partnerMember) {
		return PartnerElement.builder()
			.id(partnerMember.getId())
			.groupId(group.getId())
			.name(partnerMember.getName())
			.imageUrl(partnerMember.getImageUrl())
			.creditScore(partnerMember.getCreditScore())
			.build();
	}
}
