package com.pangpang.airbank.domain.fund.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.fund.dto.GetTaxResponseDto;
import com.pangpang.airbank.domain.fund.service.FundService;
import com.pangpang.airbank.global.aop.CheckGroup;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.Authentication;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *  자금에 대한 Controller
 */
@RestController
@RequestMapping("/funds")
@RequiredArgsConstructor
@Slf4j
public class FundController {
	private final FundService fundService;

	/**
	 *  현재 세금 현황 조회
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < GetTaxResponseDto>>
	 * @see FundService
	 */
	@CheckGroup
	@GetMapping("/tax")
	public ResponseEntity<EnvelopeResponse<GetTaxResponseDto>> getTax(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestParam("group_id") Long groupId) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<GetTaxResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(
					fundService.getTax(authenticatedMemberArgument.getMemberId(), groupId)
				)
				.build());
	}
}
