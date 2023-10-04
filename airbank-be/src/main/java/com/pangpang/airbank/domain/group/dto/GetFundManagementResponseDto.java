package com.pangpang.airbank.domain.group.dto;

import com.pangpang.airbank.domain.fund.domain.FundManagement;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetFundManagementResponseDto {
	private Integer taxRate;
	private Long allowanceAmount;
	private Integer allowanceDate;
	private Integer confiscationRate;
	private Long loanLimit;

	public static GetFundManagementResponseDto from(FundManagement fundManagement) {
		return GetFundManagementResponseDto.builder()
			.taxRate(fundManagement.getTaxRate())
			.allowanceAmount(fundManagement.getAllowanceAmount())
			.allowanceDate(fundManagement.getAllowanceDate())
			.confiscationRate(fundManagement.getConfiscationRate())
			.loanLimit(fundManagement.getLoanLimit())
			.build();
	}
}
