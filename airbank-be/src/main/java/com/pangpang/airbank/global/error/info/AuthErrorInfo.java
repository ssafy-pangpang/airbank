package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum AuthErrorInfo {
	UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, 1100, "인증이 유효하지 않습니다."),
	AUTH_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1101, "인증 서버와의 통신에 실패했습니다."),
	INVALID_AUTH_CODE(HttpStatus.BAD_REQUEST, 1102, "인증 코드가 유효하지 않습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	AuthErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
