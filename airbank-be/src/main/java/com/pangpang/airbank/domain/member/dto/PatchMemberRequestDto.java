package com.pangpang.airbank.domain.member.dto;

import com.pangpang.airbank.global.meta.domain.MemberRole;

import lombok.Getter;

@Getter
public class PatchMemberRequestDto {
	private String name;
	private String phoneNumber;
	private MemberRole role;
}
