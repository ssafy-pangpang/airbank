package com.pangpang.airbank.domain.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.domain.account.service.AccountService;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.Authentication;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *  계좌 정보에 대한 Controller
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

	private final AccountService accountService;

	/**
	 *  사용자가 계좌 등록할 때 처리
	 *
	 * @param postEnrollAccountRequestDto PostEnrollAccountRequestDto
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @return ResponseEntity<EnvelopeResponse < CommonIdResponseDto>>
	 * @see AccountService
	 */
	@PostMapping()
	public ResponseEntity<EnvelopeResponse<CommonIdResponseDto>> enrollAccount(
		@RequestBody PostEnrollAccountRequestDto postEnrollAccountRequestDto,
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument) {

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(EnvelopeResponse.<CommonIdResponseDto>builder()
				.code(HttpStatus.CREATED.value())
				.data(
					accountService.saveAccount(postEnrollAccountRequestDto, authenticatedMemberArgument.getMemberId())
				)
				.build());
	}
}
