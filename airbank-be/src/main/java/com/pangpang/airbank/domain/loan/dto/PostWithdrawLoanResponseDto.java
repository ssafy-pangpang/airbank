package com.pangpang.airbank.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostWithdrawLoanResponseDto {
	private Long amount;

	public static PostWithdrawLoanResponseDto from(Long amount) {
		return PostWithdrawLoanResponseDto.builder()
			.amount(amount)
			.build();
	}
}
