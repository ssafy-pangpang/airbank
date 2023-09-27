package com.pangpang.airbank.domain.member.dto;

import com.pangpang.airbank.domain.auth.dto.PostLoginResponseDto;
import com.pangpang.airbank.domain.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginMemberResponseDto {
	private Long id;
	private String name;
	private String phoneNumber;

	public static LoginMemberResponseDto from(Member member) {
		return LoginMemberResponseDto.builder()
			.id(member.getId())
			.name(member.getName())
			.phoneNumber(member.getPhoneNumber())
			.build();
	}
}
