package com.pangpang.airbank.global.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonAmountResponseDto {
	private Long amount;

	public static CommonAmountResponseDto from(Long amount) {
		return CommonAmountResponseDto.builder()
			.amount(amount)
			.build();
	}
}
