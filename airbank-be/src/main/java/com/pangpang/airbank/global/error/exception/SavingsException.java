package com.pangpang.airbank.global.error.exception;

import com.pangpang.airbank.global.error.info.SavingsErrorInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SavingsException extends RuntimeException {
	private final SavingsErrorInfo info;
}
