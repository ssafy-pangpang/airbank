package com.pangpang.airbank.domain.auth.dto;

import com.pangpang.airbank.domain.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetLogoutResponseDto {
	private String name;

	public GetLogoutResponseDto(Member member) {
		this.name = member.getName();
	}
}
