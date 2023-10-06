package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum MetaErrorInfo {
	INVALID_METADATA(HttpStatus.BAD_REQUEST, 1900, "유효하지 않은 메타데이터 입니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	MetaErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
