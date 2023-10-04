package com.pangpang.airbank.global.meta.domain;

import java.util.Arrays;

import com.pangpang.airbank.global.error.exception.MetaException;
import com.pangpang.airbank.global.error.info.MetaErrorInfo;

import lombok.Getter;

@Getter
public enum TransactionType {
	TAX(1, "TAX", "세금"),
	INTEREST(2, "INTEREST", "이자"),
	BONUS(3, "BONUS", "보너스"),
	ALLOWANCE(4, "ALLOWANCE", "용돈"),
	MISSION(5, "MISSION", "미션"),
	CONFISCATION(6, "CONFISCATION", "압류"),
	LOAN(7, "LOAN", "땡겨쓰기"),
	SAVINGS(8, "SAVINGS", "티끌 모으기"),
	TAX_REFUND(9, "TAX_REFUND", "세금 환금");

	private final Integer id;
	private final String name;
	private final String msgName;

	TransactionType(int id, String name, String msgName) {
		this.id = id;
		this.name = name;
		this.msgName = msgName;
	}

	public static TransactionType ofName(String name) {
		return Arrays.stream(TransactionType.values())
			.filter(value -> value.getName().equals(name))
			.findAny()
			.orElseThrow(() -> new MetaException(MetaErrorInfo.INVALID_METADATA));
	}
}
