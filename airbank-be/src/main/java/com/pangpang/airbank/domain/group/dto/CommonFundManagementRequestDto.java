package com.pangpang.airbank.domain.group.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonFundManagementRequestDto {
	private Integer taxRate;
	private Long allowanceAmount;
	private Integer allowanceDate;
	private Integer confiscationRate;
	private Long loanLimit;

}
