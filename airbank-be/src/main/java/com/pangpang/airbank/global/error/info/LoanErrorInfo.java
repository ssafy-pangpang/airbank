package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum LoanErrorInfo {
	ALREADY_HAD_PARENT(HttpStatus.BAD_REQUEST, 1400, "");
	private final HttpStatus status;
	private final Integer code;
	private final String message;

	LoanErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
