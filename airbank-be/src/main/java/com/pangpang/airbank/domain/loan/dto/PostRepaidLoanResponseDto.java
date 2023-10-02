package com.pangpang.airbank.domain.loan.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRepaidLoanResponseDto {
	private Long amount;
	private Long loanBalance;

	public static PostRepaidLoanResponseDto of(Long amount, Long loanAmount) {
		return PostRepaidLoanResponseDto.builder()
			.amount(amount)
			.loanBalance(loanAmount)
			.build();
	}
}
