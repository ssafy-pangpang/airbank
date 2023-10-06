package com.pangpang.airbank.global.meta.domain;

import java.util.Arrays;

import com.pangpang.airbank.global.error.exception.MetaException;
import com.pangpang.airbank.global.error.info.MetaErrorInfo;

import lombok.Getter;

@Getter
public enum SavingsStatus {
	PENDING(1, "PENDING"),
	PROCEEDING(2, "PROCEEDING"),
	SUCCESS(3, "SUCCESS"),
	FAIL(4, "FAIL"),
	REJECT(5, "REJECT");

	private final Integer id;
	private final String name;

	SavingsStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static SavingsStatus ofName(String name) {
		return Arrays.stream(SavingsStatus.values())
			.filter(value -> value.getName().equals(name))
			.findAny()
			.orElseThrow(() -> new MetaException(MetaErrorInfo.INVALID_METADATA));
	}
}
