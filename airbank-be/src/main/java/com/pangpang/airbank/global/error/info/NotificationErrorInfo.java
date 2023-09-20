package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

public enum NotificationErrorInfo {
	NOT_FOUND_NOTIFICATION_BY_MEMBER_ID_AND_PARTNER_ID(HttpStatus.NOT_FOUND, 1701, "등록된 알림이 없습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	NotificationErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
