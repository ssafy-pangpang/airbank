package com.pangpang.airbank.domain.savings.dto;

import java.time.LocalDate;

import com.pangpang.airbank.domain.savings.domain.Savings;
import com.pangpang.airbank.domain.savings.domain.SavingsItem;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetCurrentSavingsResponseDto {
	private Long id;
	private Long myAmount;
	private Long parentsAmount;
	private Long monthlyAmount;
	private LocalDate startedAt;
	private LocalDate expiredAt;
	private LocalDate endedAt;
	private String status;
	private Integer delayCount;
	private SavingsElement savingsItem;

	public static GetCurrentSavingsResponseDto of(Savings savings, SavingsItem savingsItem) {
		return GetCurrentSavingsResponseDto.builder()
			.id(savings.getId())
			.myAmount(savings.getMyAmount())
			.parentsAmount(savings.getParentsAmount())
			.monthlyAmount(savings.getMonthlyAmount())
			.startedAt(savings.getStartedAt())
			.expiredAt(savings.getExpiredAt())
			.endedAt(savings.getEndedAt())
			.status(savings.getStatus().getName())
			.delayCount(savings.getMonth() - savings.getPaymentCount())
			.savingsItem(SavingsElement.from(savingsItem))
			.build();
	}
}
