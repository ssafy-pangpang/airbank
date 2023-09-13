package com.pangpang.airbank.global.error.exception;

import com.pangpang.airbank.global.error.info.MetaErrorInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MetaException extends RuntimeException {
	private final MetaErrorInfo info;
}
