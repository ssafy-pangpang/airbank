package com.pangpang.airbank.domain.member.dto;

import com.pangpang.airbank.domain.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatchMemberResponseDto {
	private String name;
	private String phoneNumber;
	private String role;

	public static PatchMemberResponseDto from(Member member) {
		return PatchMemberResponseDto.builder()
			.name(member.getName())
			.phoneNumber(member.getPhoneNumber())
			.role(member.getRole().getName())
			.build();
	}
}
