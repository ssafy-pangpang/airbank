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

	public static GetLoginMemberResponseDto from(Long id, GetLoginResponseDto getLoginResponseDto) {
		return GetLoginMemberResponseDto.builder()
			.id(id)
			.getLoginResponseDto(getLoginResponseDto)
			.build();
	}
}
