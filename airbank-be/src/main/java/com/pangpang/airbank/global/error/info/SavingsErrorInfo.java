package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SavingsErrorInfo {
	NOT_FOUND_SAVINGS_IN_PROCEEDING(HttpStatus.NOT_FOUND, 1800, "진행중인 티끌모으기를 찾을 수 없습니다."),
	NOT_FOUND_SAVINGS_ITEM(HttpStatus.NOT_FOUND, 1801, "티끌모으기 상품 정보를 찾을 수 없습니다."),
	ENROLL_SAVINGS_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1802, "티끌모으기는 자녀만 등록할 수 있습니다."),
	ALREADY_SAVINGS_IN_PROCEEDING(HttpStatus.FORBIDDEN, 1803, "이미 진행중인 티끌모으기 존재합니다."),
	CONFIRM_SAVINGS_PERMISSION_DENIE(HttpStatus.FORBIDDEN, 1804, "티끌모으기 수락/거절 권한이 없습니다."),
	NOT_FOUND_SAVINGS_IN_PENDING(HttpStatus.NOT_FOUND, 1805, "등록 대기중인 티끌모으기를 찾을 수 없습니다."),
	ALREADY_SAVINGS_IN_PENDING(HttpStatus.FORBIDDEN, 1806, "이미 등록 대기중인 티끌모으기가 존재합니다."),
	CANCEL_SAVINGS_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1807, "티끌모으기 포기는 자녀만 가능합니다."),
	ALREADY_EXIT_SAVINGS(HttpStatus.FORBIDDEN, 1808, "이미 종료된 티끌모으기 입니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	SavingsErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
