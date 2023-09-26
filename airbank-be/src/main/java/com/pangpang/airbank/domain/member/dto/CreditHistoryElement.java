package com.pangpang.airbank.domain.member.dto;

import java.time.LocalDateTime;

import com.pangpang.airbank.domain.member.domain.CreditHistory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditHistoryElement {
	private Integer creditScore;
	private LocalDateTime createdAt;

	public static CreditHistoryElement from(CreditHistory creditHistory) {
		return CreditHistoryElement.builder()
			.creditScore(creditHistory.getCreditScore())
			.createdAt(creditHistory.getCreatedAt())
			.build();
	}
}
