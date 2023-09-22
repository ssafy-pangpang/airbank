package com.pangpang.airbank.domain.group.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.pangpang.airbank.domain.group.domain.Group;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPartnersResponseDto {
	private List<PartnerElement> members;

	public static GetPartnersResponseDto of(List<Group> groups, Long memberId) {
		return GetPartnersResponseDto.builder()
			.members(groups.stream()
				.map(group -> PartnerElement.of(group,
					group.getPartnerMember(memberId)))
				.collect(Collectors.toList()))
			.build();
	}
}
