package com.pangpang.airbank.domain.group.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatchFundManagementResponseDto {
	private Integer taxRate;
	private Long allowanceAmount;
	private Integer allowanceDate;
	private Integer confiscationRate;
	private Long loanLimit;

	public static PatchFundManagementResponseDto from(CommonFundManagementRequestDto commonFundManagementRequestDto) {
		return PatchFundManagementResponseDto.builder()
			.taxRate(commonFundManagementRequestDto.getTaxRate())
			.allowanceAmount(commonFundManagementRequestDto.getAllowanceAmount())
			.allowanceDate(commonFundManagementRequestDto.getAllowanceDate())
			.confiscationRate(commonFundManagementRequestDto.getConfiscationRate())
			.loanLimit(commonFundManagementRequestDto.getLoanLimit())
			.build();
	}
}
