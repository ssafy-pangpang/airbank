package com.pangpang.airbank.global.error.exception;

import com.pangpang.airbank.global.error.info.AuthErrorInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthException extends RuntimeException {
	private final AuthErrorInfo info;

}
