package com.pangpang.airbank.global.meta.domain;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.pangpang.airbank.global.error.exception.MetaException;
import com.pangpang.airbank.global.error.info.MetaErrorInfo;

import lombok.Getter;

/**
 * 세금 환급 비율
 */
@Getter
public enum TaxRefund {
	ONE(1, 1.0),
	TWO(2, 0.5),
	THREE(3, 0.2);

	private final Integer creditRating;
	private final Double refundRatio;

	TaxRefund(Integer creditRating, Double refundRatio) {
		this.creditRating = creditRating;
		this.refundRatio = refundRatio;
	}

	@JsonCreator
	public static TaxRefund ofCreditRating(Integer creditRating) {
		return Arrays.stream(TaxRefund.values())
			.filter(value -> value.getCreditRating().equals(creditRating))
			.findAny()
			.orElseThrow(() -> new MetaException(MetaErrorInfo.INVALID_METADATA));
	}
}
