package com.pangpang.airbank.global.error.exception;

import com.pangpang.airbank.global.error.info.FundErrorInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FundException extends RuntimeException {
	private final FundErrorInfo info;
}
