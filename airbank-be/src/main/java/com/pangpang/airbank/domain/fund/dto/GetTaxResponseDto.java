package com.pangpang.airbank.domain.fund.dto;

import java.time.LocalDate;

import com.pangpang.airbank.domain.fund.domain.Tax;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetTaxResponseDto {
	private Long amount;
	private Long overdueAmount;
	private LocalDate expiredAt;

	public static GetTaxResponseDto of(Tax tax, Long overdueAmount) {
		return GetTaxResponseDto.builder()
			.amount(tax.getAmount() == null ? 0L : tax.getAmount())
			.overdueAmount(overdueAmount == null ? 0L : overdueAmount)
			.expiredAt(tax.getExpiredAt())
			.build();
	}
}
