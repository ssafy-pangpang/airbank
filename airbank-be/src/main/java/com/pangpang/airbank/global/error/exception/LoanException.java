package com.pangpang.airbank.global.error.exception;

import com.pangpang.airbank.global.error.info.LoanErrorInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoanException extends RuntimeException {
	private final LoanErrorInfo info;
}