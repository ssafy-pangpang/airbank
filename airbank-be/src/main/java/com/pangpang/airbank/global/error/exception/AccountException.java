package com.pangpang.airbank.global.error.exception;

import com.pangpang.airbank.global.error.info.AccountErrorInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountException extends RuntimeException {
	private final AccountErrorInfo info;

}
