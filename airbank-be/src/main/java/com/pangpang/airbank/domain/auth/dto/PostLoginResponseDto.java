package com.pangpang.airbank.domain.auth.dto;

import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.dto.LoginMemberResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLoginResponseDto {
	private String name;
	private String phoneNumber;

	public static PostLoginResponseDto from(LoginMemberResponseDto loginMemberResponseDto) {
		return PostLoginResponseDto.builder()
			.name(loginMemberResponseDto.getName())
			.phoneNumber(loginMemberResponseDto.getPhoneNumber())
			.build();
	}
}
