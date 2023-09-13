package com.pangpang.airbank.global.error.exception;

import com.pangpang.airbank.global.error.info.MemberErrorInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberException extends RuntimeException {
	private final MemberErrorInfo info;
}
