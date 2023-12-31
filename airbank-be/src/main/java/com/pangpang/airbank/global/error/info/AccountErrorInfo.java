package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum AccountErrorInfo {
	ACCOUNT_NH_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1000, "NH API 서버와의 통신에 실패했습니다."),
	ACCOUNT_REQUEST_DATA_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "요청 정보가 잘못됐습니다."),
	ACCOUNT_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1002, "Account 통신에 실패했습니다."),
	ACCOUNT_ENROLL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1003, "Account 등록에 실패했습니다."),
	NOT_FOUND_ACCOUNT(HttpStatus.NOT_FOUND, 1004, "등록된 계좌가 없습니다."),
	NOT_FOUND_AVAILABLE_ACCOUNT(HttpStatus.NOT_FOUND, 1005, "사용가능한 계좌가 없습니다."),
	NOT_FOUND_LOAN_ACCOUNT(HttpStatus.NOT_FOUND, 1006, "등록된 땡겨쓰기 계좌가 없습니다."),
	ACCOUNT_AMOUNT_SERVER_ERROR(HttpStatus.NOT_FOUND, 1007, "계좌의 잔액을 불러올 수 없습니다."),
	NOT_FOUND_SAVINGS_ACCOUNT(HttpStatus.NOT_FOUND, 1008, "등록된 티끌모으기 계좌가 없습니다."),
	ACCOUNT_HISTORY_SERVER_ERROR(HttpStatus.NOT_FOUND, 1009, "계좌의 내역을 불러올 수 없습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	AccountErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
