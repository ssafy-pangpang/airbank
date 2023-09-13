package com.pangpang.airbank.global.meta.domain;

import lombok.Getter;

@Getter
public enum CreditRating {
	ONE(1, 900, 1000),
	TWO(2, 870, 899),
	THREE(3, 840, 869),
	FOUR(4, 805, 839),
	FIVE(5, 750, 804),
	SIX(6, 665, 749),
	SEVEN(7, 600, 664),
	EIGHT(8, 515, 599),
	NINE(9, 445, 514),
	TEN(10, 0, 444);

	private final Integer rating;
	private final Integer minScore;
	private final Integer maxScore;

	CreditRating(Integer rating, Integer minScore, Integer maxScore) {
		this.rating = rating;
		this.minScore = minScore;
		this.maxScore = maxScore;
	}
}
