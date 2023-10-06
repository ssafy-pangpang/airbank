package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum AuthErrorInfo {
	UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, 1100, "인증이 유효하지 않습니다."),
	INVALID_OAUTHIDENTIFIER(HttpStatus.BAD_REQUEST, 1101, "인증 식별자가 유효하지 않습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	AuthErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
