package com.pangpang.airbank.global.meta.domain;

import lombok.Getter;

@Getter
public enum InterestRate {
	ONE(1, 1),
	TWO(2, 2),
	THREE(3, 3),
	FOUR(4, 4),
	FIVE(5, 5),
	SIX(6, 5),
	SEVEN(7, 5),
	EIGHT(8, 5),
	NINE(9, 5),
	TEN(10, 5);

	private final Integer rating;
	private final Integer interestRate;

	InterestRate(Integer rating, Integer interestRate) {
		this.rating = rating;
		this.interestRate = interestRate;
	}
}
