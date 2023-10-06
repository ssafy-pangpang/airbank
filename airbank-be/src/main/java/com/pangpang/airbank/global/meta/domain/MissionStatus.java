package com.pangpang.airbank.global.meta.domain;

import java.util.Arrays;

import com.pangpang.airbank.global.error.exception.MetaException;
import com.pangpang.airbank.global.error.info.MetaErrorInfo;

import lombok.Getter;

@Getter
public enum MissionStatus {
	PENDING(1, "PENDING"),
	AVAILABLE(2, "AVAILABLE"),
	PROCEEDING(3, "PROCEEDING"),
	COMPLETE(4, "COMPLETE"),
	SUCCESS(5, "SUCCESS"),
	FAIL(6, "FAIL"),
	REJECT(7, "REJECT");

	private final Integer id;
	private final String name;

	MissionStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MissionStatus ofName(String name) {
		return Arrays.stream(MissionStatus.values())
			.filter(value -> value.getName().equals(name))
			.findAny()
			.orElseThrow(() -> new MetaException(MetaErrorInfo.INVALID_METADATA));
	}
}
