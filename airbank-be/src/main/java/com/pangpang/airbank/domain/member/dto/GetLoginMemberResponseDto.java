package com.pangpang.airbank.domain.member.dto;

import com.pangpang.airbank.domain.auth.dto.GetLoginResponseDto;
import com.pangpang.airbank.domain.member.domain.Member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetLoginMemberResponseDto {
	private Long id;
	private GetLoginResponseDto getLoginResponseDto;

	public static GetLoginMemberResponseDto from(Member member) {
		return GetLoginMemberResponseDto.builder()
			.id(member.getId())
			.getLoginResponseDto(GetLoginResponseDto.from(member))
			.build();
	}
}
