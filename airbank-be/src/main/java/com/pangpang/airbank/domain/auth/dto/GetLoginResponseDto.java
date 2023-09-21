package com.pangpang.airbank.domain.auth.dto;

import com.pangpang.airbank.domain.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetLoginResponseDto {
	private String name;
	private String phoneNumber;

	public static GetLoginResponseDto from(Member member) {
		return GetLoginResponseDto.builder()
			.name(member.getName())
			.phoneNumber(member.getPhoneNumber())
			.build();
	}
}
