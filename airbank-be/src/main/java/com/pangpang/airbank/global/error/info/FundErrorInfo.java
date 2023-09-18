package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum FundErrorInfo {
	NOT_FOUND_FUND_MANAGEMENT_BY_MEMBER_RELATIONSHIP_ID(HttpStatus.UNAUTHORIZED, 1200, "자금 관리를 찾을 수 없습니다."),
	UPDATE_FUND_MANAGEMENT_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1201, "자금 관리를 수정할 권한이 없습니다."),
	SAVE_FUND_MANAGEMENT_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1202, "자금 관리를 저장할 권한이 없습니다."),
	ALREADY_EXISTS_FUND_MANAGEMENT(HttpStatus.CONFLICT, 1203, "자금 관리가 이미 존재합니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	FundErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
