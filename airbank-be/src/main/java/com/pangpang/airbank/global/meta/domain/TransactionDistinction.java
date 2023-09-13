package com.pangpang.airbank.global.meta.domain;

import java.util.Arrays;

import com.pangpang.airbank.global.error.exception.MetaException;
import com.pangpang.airbank.global.error.info.MetaErrorInfo;

import lombok.Getter;

@Getter
public enum TransactionDistinction {
	DEPOSIT(1, "DEPOSIT"),
	WITHDRAW(2, "WITHDRAW");

	private final Integer id;
	private final String name;

	TransactionDistinction(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static TransactionDistinction ofName(String name) {
		return Arrays.stream(TransactionDistinction.values())
			.filter(value -> value.getName().equals(name))
			.findAny()
			.orElseThrow(() -> new MetaException(MetaErrorInfo.INVALID_METADATA));
	}
}
