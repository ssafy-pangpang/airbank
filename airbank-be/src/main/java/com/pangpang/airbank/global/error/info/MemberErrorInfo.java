package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum MemberErrorInfo {
	NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, 100, "사용자를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	MemberErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
