package com.pangpang.airbank.global.error.exception;

import com.pangpang.airbank.global.error.info.NotificationErrorInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationException extends RuntimeException {
	private final NotificationErrorInfo info;
}
