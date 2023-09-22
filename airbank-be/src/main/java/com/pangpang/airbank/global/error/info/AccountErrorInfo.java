package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum AccountErrorInfo {
	ACCOUNT_NH_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1000, "NH API 서버와의 통신에 실패했습니다."),
	ACCOUNT_REQUEST_DATA_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "요청 정보가 잘못됐습니다."),
	ACCOUNT_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1002, "Account 통신에 실패했습니다."),
	ACCOUNT_ENROLL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1003, "Account 등록에 실패했습니다."),
	NOT_FOUND_ACCOUNT(HttpStatus.NOT_FOUND, 1004, "등록된 계좌가 없습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	AccountErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
