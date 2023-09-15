package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum GroupErrorInfo {
	ALREADY_HAD_PARENT(HttpStatus.BAD_REQUEST, 1301, "이미 부모가 존재하는 자녀입니다."),
	ENROLL_IN_PROGRESS(HttpStatus.CONFLICT, 1302, "이미 자녀 등록이 진행 중 입니다."),
	ENROLL_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1303, "자녀를 등록할 권한이 없습니다."),
	NOT_FOUND_MEMBER_RELATIONSHIP_BY_CHILD(HttpStatus.NOT_FOUND, 1304, "등록중인 사용자 관계가 없습니다."),
	CONFIRM_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1305, "자녀만 접근할 수 있습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	GroupErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
