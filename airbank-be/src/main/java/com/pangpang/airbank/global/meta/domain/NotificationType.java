package com.pangpang.airbank.global.meta.domain;

import java.util.Arrays;

import com.pangpang.airbank.global.error.exception.MetaException;
import com.pangpang.airbank.global.error.info.MetaErrorInfo;

import lombok.Getter;

@Getter
public enum NotificationType {
	TAX(1, "TAX"),
	INTEREST(2, "INTEREST"),
	BONUS(3, "BONUS"),
	ALLOWANCE(4, "ALLOWANCE"),
	MISSION(5, "MISSION"),
	CONFISCATION(6, "CONFISCATION"),
	LOAN(7, "LOAN"),
	SAVINGS(8, "SAVINGS");

	private final Integer id;
	private final String name;

	NotificationType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static NotificationType ofName(String name) {
		return Arrays.stream(NotificationType.values())
			.filter(value -> value.getName().equals(name))
			.findAny()
			.orElseThrow(() -> new MetaException(MetaErrorInfo.INVALID_METADATA));
	}
}
