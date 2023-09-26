package com.pangpang.airbank.domain.account.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransferResponseDto {
	private Long amount;

	public static TransferResponseDto from(Long amount) {
		return TransferResponseDto.builder()
			.amount(amount)
			.build();
	}
}
