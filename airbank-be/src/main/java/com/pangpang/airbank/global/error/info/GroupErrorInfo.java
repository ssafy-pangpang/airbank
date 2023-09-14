package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum GroupErrorInfo {
	UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, 1301, "~~~");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	GroupErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
