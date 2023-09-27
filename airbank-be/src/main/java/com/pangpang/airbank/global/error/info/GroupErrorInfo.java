package com.pangpang.airbank.global.error.info;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum GroupErrorInfo {
	ALREADY_HAD_PARENT(HttpStatus.BAD_REQUEST, 1300, "이미 부모가 존재하는 자녀입니다."),
	ENROLL_IN_PROGRESS(HttpStatus.CONFLICT, 1301, "이미 자녀 등록이 진행 중 입니다."),
	ENROLL_CHILD_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1302, "자녀를 등록할 권한이 없습니다."),
	NOT_FOUND_GROUP_BY_CHILD(HttpStatus.NOT_FOUND, 1303, "등록중인 그룹이 없습니다."),
	CONFIRM_ENROLLMENT_PERMISSION_DENIED(HttpStatus.FORBIDDEN, 1304, "자녀만 접근할 수 있습니다."),
	NOT_FOUND_GROUP_BY_PARENT_ID(HttpStatus.NOT_FOUND, 1305, "그룹을 찾을 수 없습니다."),
	NOT_FOUND_GROUP_BY_ID_AND_MEMBER_ID(HttpStatus.FORBIDDEN, 1306, "사용자가 해당 그룹에 속해있지 않습니다."),
	NOT_FOUND_GROUP_BY_ID(HttpStatus.NOT_FOUND, 1307, "그룹을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final Integer code;
	private final String message;

	GroupErrorInfo(HttpStatus status, Integer code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
