package com.pangpang.airbank.domain.fund.dto;

import java.time.LocalDate;

import com.pangpang.airbank.domain.fund.domain.Interest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetInterestResponseDto {
	private static final Long DEFAULT_AMOUNT = 0L;
	private Long amount;
	private Long overdueAmount;
	private LocalDate expiredAt;

	public static GetInterestResponseDto of(Interest interest, Long overdueAmount) {
		return GetInterestResponseDto.builder()
			.amount(interest.getAmount() == null ? DEFAULT_AMOUNT : interest.getAmount())
			.overdueAmount(overdueAmount == null ? DEFAULT_AMOUNT : overdueAmount)
			.expiredAt(interest.getExpiredAt())
			.build();
	}
}
