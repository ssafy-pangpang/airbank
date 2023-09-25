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

	public static CreditRating getCreditRating(Integer creditScore) {
		if (creditScore >= ONE.minScore && creditScore <= ONE.maxScore) {
			return ONE;
		}
		if (creditScore >= TWO.minScore && creditScore <= TWO.maxScore) {
			return TWO;
		}
		if (creditScore >= THREE.minScore && creditScore <= THREE.maxScore) {
			return THREE;
		}
		if (creditScore >= FOUR.minScore && creditScore <= FOUR.maxScore) {
			return FOUR;
		}
		if (creditScore >= FIVE.minScore && creditScore <= FIVE.maxScore) {
			return FIVE;
		}
		if (creditScore >= SIX.minScore && creditScore <= SIX.maxScore) {
			return SIX;
		}
		if (creditScore >= SEVEN.minScore && creditScore <= SEVEN.maxScore) {
			return SEVEN;
		}
		if (creditScore >= EIGHT.minScore && creditScore <= EIGHT.maxScore) {
			return EIGHT;
		}
		if (creditScore >= NINE.minScore && creditScore <= NINE.maxScore) {
			return NINE;
		}
		if (creditScore >= TEN.minScore && creditScore <= TEN.maxScore) {
			return TEN;
		}
		return null;
	}
}
