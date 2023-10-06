package com.pangpang.airbank.global.meta.domain;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.pangpang.airbank.global.error.exception.MetaException;
import com.pangpang.airbank.global.error.info.MetaErrorInfo;

import lombok.Getter;

@Getter
public enum MemberRole {
	PARENT(1, "PARENT"),
	CHILD(2, "CHILD"),
	UNKNOWN(3, "UNKNOWN");

	private final Integer id;
	private final String name;

	MemberRole(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@JsonCreator
	public static MemberRole ofName(String name) {
		return Arrays.stream(MemberRole.values())
			.filter(value -> value.getName().equals(name))
			.findAny()
			.orElseThrow(() -> new MetaException(MetaErrorInfo.INVALID_METADATA));
	}

	@JsonValue
	public String getName() {
		return name;
	}
}
