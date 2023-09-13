package com.pangpang.airbank.domain.member.dto;

import com.pangpang.airbank.domain.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetMemberResponseDto {
	private Long id;
	private String name;
	private String phoneNumber;
	private String imageUrl;
	private String role;

	public static GetMemberResponseDto from(Member member) {
		return GetMemberResponseDto.builder()
			.id(member.getId())
			.name(member.getName())
			.phoneNumber(member.getPhoneNumber())
			.imageUrl(member.getImageUrl())
			.role(member.getRole().getName())
			.build();
	}
}
