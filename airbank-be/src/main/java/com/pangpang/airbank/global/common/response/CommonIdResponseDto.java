package com.pangpang.airbank.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *  Id만 반환하는 Resposne Dto
 */
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommonIdResponseDto {
	private Long id;

	public static CommonIdResponseDto from(Long id) {
		return CommonIdResponseDto.builder()
			.id(id)
			.build();
	}
}
