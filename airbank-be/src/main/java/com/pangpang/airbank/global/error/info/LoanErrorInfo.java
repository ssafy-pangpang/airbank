package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum LoanErrorInfo {
	WITHDRAW_LOAN_PERMISSION_DENIED(HttpStatus.BAD_REQUEST, 1400, "땡겨쓰기는 자녀만 사용할 수 있습니다."),
	CREDIT_SCORE_BELOW_THRESHOLD(HttpStatus.FORBIDDEN, 1401, "신용등급이 낮습니다."),
	LOAN_BALANCE_LIMIT_EXCEEDED(HttpStatus.FORBIDDEN, 1402, "땡겨쓰기 계좌 잔액이 부족합니다."),
	NOT_PAID_INTEREST(HttpStatus.FORBIDDEN, 1403, "이자가 상환되지 않았습니다."),
	LOAN_BALANCE_REPAID_AMOUNT_EXCEEDED(HttpStatus.FORBIDDEN, 1404, "상환금액이 땡겨쓰기 한도를 초과했습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	LoanErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
