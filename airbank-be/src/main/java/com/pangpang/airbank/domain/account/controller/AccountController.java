package com.pangpang.airbank.domain.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.account.dto.CommonAccountIdResponseDto;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.domain.account.service.AccountService;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

	private final AccountService accountService;

	@PostMapping()
	public ResponseEntity<EnvelopeResponse<CommonAccountIdResponseDto>> enrollAccount(
		@RequestBody PostEnrollAccountRequestDto postEnrollAccountRequestDto) {
		AuthenticatedMemberArgument member = new AuthenticatedMemberArgument(1L);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(EnvelopeResponse.<CommonAccountIdResponseDto>builder()
				.code(HttpStatus.CREATED.value())
				.data(accountService.saveAccount(postEnrollAccountRequestDto, member.getMemberId()))
				.build());
	}
}
