package com.pangpang.airbank.domain.member.dto;

import lombok.Getter;

@Getter
public class PatchMemberRequestDto {
	private String name;
	private String phoneNumber;
	private String role;
}
