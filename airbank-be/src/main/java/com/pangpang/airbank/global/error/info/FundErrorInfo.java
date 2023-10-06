package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum FundErrorInfo {
	NOT_FOUND_FUND_MANAGEMENT_BY_GROUP(HttpStatus.UNAUTHORIZED, 1200, "자금 관리를 찾을 수 없습니다."),
	UPDATE_FUND_MANAGEMENT_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1201, "자금 관리를 수정할 권한이 없습니다."),
	SAVE_FUND_MANAGEMENT_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1202, "자금 관리를 저장할 권한이 없습니다."),
	ALREADY_EXISTS_FUND_MANAGEMENT(HttpStatus.CONFLICT, 1203, "자금 관리가 이미 존재합니다."),
	NOT_MATCH_AMOUNT(HttpStatus.CONFLICT, 1204, "송금된 금액이 지정 금액과 다릅니다."),
	NOT_FOUND_TRANSFER_AMOUNT(HttpStatus.UNAUTHORIZED, 1205, "송금될 금액이 없습니다."),
	REPAY_CONFISCATION_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1206, "변제금을 송금할 권한이 없습니다."),
	NOT_FOUND_CONFISCATION_BY_GROUP(HttpStatus.UNAUTHORIZED, 1207, "압류중인 재산을 찾을 수 없습니다."),
	CONFISCATION_AMOUNT_EXCEEDED(HttpStatus.UNAUTHORIZED, 1208, "상환할 수 있는 변제금을 초과했습니다."),
	NOT_FOUND_TAX_BY_GROUP(HttpStatus.UNAUTHORIZED, 1209, "세금을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	FundErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
