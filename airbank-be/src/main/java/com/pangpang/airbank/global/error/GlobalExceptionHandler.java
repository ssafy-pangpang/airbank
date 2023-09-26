package com.pangpang.airbank.global.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.exception.AuthException;
import com.pangpang.airbank.global.error.exception.FundException;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.LoanException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.exception.MetaException;
import com.pangpang.airbank.global.error.exception.SavingsException;

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

	@ExceptionHandler(GroupException.class)
	public ResponseEntity<EnvelopeResponse<GroupException>> groupExceptionHandler(GroupException exception) {
		return ResponseEntity.status(exception.getInfo().getStatus())
			.body(EnvelopeResponse.<GroupException>builder()
				.code(exception.getInfo().getCode())
				.message(exception.getInfo().getMessage())
				.build());
	}

	@ExceptionHandler(FundException.class)
	public ResponseEntity<EnvelopeResponse<FundException>> fundExceptionHandler(FundException exception) {
		return ResponseEntity.status(exception.getInfo().getStatus())
			.body(EnvelopeResponse.<FundException>builder()
				.code(exception.getInfo().getCode())
				.message(exception.getInfo().getMessage())
				.build());
	}

	@ExceptionHandler(AccountException.class)
	public ResponseEntity<EnvelopeResponse<AccountException>> accountExceptionHandler(AccountException exception) {
		return ResponseEntity.status(exception.getInfo().getStatus())
			.body(EnvelopeResponse.<AccountException>builder()
				.code(exception.getInfo().getCode())
				.message(exception.getInfo().getMessage())
				.build());
	}

	@ExceptionHandler(SavingsException.class)
	public ResponseEntity<EnvelopeResponse<SavingsException>> savingsExceptionHandler(SavingsException exception) {
		return ResponseEntity.status(exception.getInfo().getStatus())
			.body(EnvelopeResponse.<SavingsException>builder()
				.code(exception.getInfo().getCode())
				.message(exception.getInfo().getMessage())
				.build());
	}

	@ExceptionHandler(LoanException.class)
	public ResponseEntity<EnvelopeResponse<LoanException>> savingsExceptionHandler(LoanException exception) {
		return ResponseEntity.status(exception.getInfo().getStatus())
			.body(EnvelopeResponse.<LoanException>builder()
				.code(exception.getInfo().getCode())
				.message(exception.getInfo().getMessage())
				.build());
	}
}
