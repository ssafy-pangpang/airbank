package com.pangpang.airbank.global.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.error.exception.AuthException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.exception.MetaException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MetaException.class)
	public ResponseEntity<EnvelopeResponse<MetaException>> metaExceptionHandler(MetaException exception) {
		return ResponseEntity.status(exception.getInfo().getStatus())
			.body(EnvelopeResponse.<MetaException>builder()
				.code(exception.getInfo().getCode())
				.message(exception.getInfo().getMessage())
				.build());
	}

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<EnvelopeResponse<MetaException>> memberExceptionHandler(MemberException exception) {
		return ResponseEntity.status(exception.getInfo().getStatus())
			.body(EnvelopeResponse.<MetaException>builder()
				.code(exception.getInfo().getCode())
				.message(exception.getInfo().getMessage())
				.build());
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<EnvelopeResponse<AuthException>> authExceptionHandler(AuthException exception) {
		return ResponseEntity.status(exception.getInfo().getStatus())
			.body(EnvelopeResponse.<AuthException>builder()
				.code(exception.getInfo().getCode())
				.message(exception.getInfo().getMessage())
				.build());
	}

}
