package com.pangpang.airbank.domain.fund.dto;

import java.time.LocalDateTime;

import com.pangpang.airbank.domain.fund.domain.Confiscation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetConfiscationResponseDto {
	private static final Long DEFAULT_AMOUNT = 0L;
	private Long amount;
	private LocalDateTime startedAt;

	public static GetConfiscationResponseDto from(Confiscation confiscation) {
		return GetConfiscationResponseDto.builder()
			.amount((confiscation.getAmount() == null) ? DEFAULT_AMOUNT : getAmount(confiscation))
			.startedAt(confiscation.getStartedAt())
			.build();
	}

	private static Long getAmount(Confiscation confiscation) {
		return confiscation.getAmount() - confiscation.getRepaidAmount();
	}
}
