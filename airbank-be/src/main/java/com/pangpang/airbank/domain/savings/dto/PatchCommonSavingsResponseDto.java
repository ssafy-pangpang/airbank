package com.pangpang.airbank.domain.savings.dto;

import com.pangpang.airbank.domain.savings.domain.Savings;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatchCommonSavingsResponseDto {
	private Long id;
	private String status;

	public static PatchCommonSavingsResponseDto from(Savings savings) {
		return PatchCommonSavingsResponseDto.builder()
			.id(savings.getId())
			.status(savings.getStatus().getName())
			.build();
	}
}
