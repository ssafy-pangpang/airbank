package com.pangpang.airbank.domain.loan.dto;

import com.pangpang.airbank.domain.fund.domain.FundManagement;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetLoanResponseDto {
	private Long amount;
	private Long loanLimit;

	public static GetLoanResponseDto from(FundManagement fundManagement) {
		return GetLoanResponseDto.builder()
			.amount(fundManagement.getLoanAmount())
			.loanLimit(fundManagement.getLoanLimit())
			.build();
	}
}
