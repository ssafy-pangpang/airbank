package com.pangpang.airbank.domain.savings.dto;

import com.pangpang.airbank.domain.savings.domain.SavingsItem;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SavingsElement {
	private String name;
	private Long amount;
	private String imageUrl;

	public static SavingsElement from(SavingsItem savingsItem) {
		return SavingsElement.builder()
			.name(savingsItem.getName())
			.amount(savingsItem.getAmount())
			.imageUrl(savingsItem.getImageUrl())
			.build();
	}
}
