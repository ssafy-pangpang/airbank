package com.pangpang.airbank.global.error.exception;

import com.pangpang.airbank.global.error.info.GroupErrorInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupException extends RuntimeException {
	private final GroupErrorInfo info;
}
