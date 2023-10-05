package com.pangpang.airbank.domain.account.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.account.dto.GetAccountAmountResponseDto;
import com.pangpang.airbank.domain.account.dto.GetAccountHistoryResponseDto;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.domain.account.service.AccountHistoryService;
import com.pangpang.airbank.domain.account.service.AccountService;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.Authentication;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *  계좌 정보에 대한 Controller
 */
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "accounts", description = "계좌 관리하는 API")
public class AccountController {

	private final AccountService accountService;
	private final AccountHistoryService accountHistoryService;

	/**
	 *  사용자가 계좌 등록할 때 처리
	 *
	 * @param postEnrollAccountRequestDto PostEnrollAccountRequestDto
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @return ResponseEntity<EnvelopeResponse < CommonIdResponseDto>>
	 * @see AccountService
	 */
	@Operation(summary = "계좌 등록", description = "사용자가 직접 계좌를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "계좌 등록 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1000", description = "NH API 서버와의 통신에 실패했습니다.", content = @Content),
		@ApiResponse(responseCode = "1003", description = "Account 등록에 실패했습니다.", content = @Content),
	})
	@PostMapping()
	public ResponseEntity<EnvelopeResponse<CommonIdResponseDto>> enrollAccount(
		@RequestBody PostEnrollAccountRequestDto postEnrollAccountRequestDto,
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument) {

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(EnvelopeResponse.<CommonIdResponseDto>builder()
				.code(HttpStatus.CREATED.value())
				.data(
					accountService.saveMainAccount(postEnrollAccountRequestDto,
						authenticatedMemberArgument.getMemberId())
				)
				.build());
	}

	/**
	 *  메인 계좌 잔액 조회
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @return ResponseEntity<EnvelopeResponse < GetAccountAmountResponseDto>>
	 * @see AccountService
	 */
	@Operation(summary = "메인 계좌 잔액 조회", description = "사용자의 메인 계좌의 잔액을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "계좌 잔액 조회 성공",
			content = @Content(schema = @Schema(implementation = GetAccountAmountResponseDto.class))),
		@ApiResponse(responseCode = "1000", description = "NH API 서버와의 통신에 실패했습니다.", content = @Content),
	})
	@GetMapping()
	public ResponseEntity<EnvelopeResponse<GetAccountAmountResponseDto>> getAccountAmount(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetAccountAmountResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(
					accountService.getAccountAmount(authenticatedMemberArgument.getMemberId())
				)
				.build());
	}

	/**
	 * 거래 내역 조회
	 *
	 * @param authenticatedMemberArgument
	 * @param accountType
	 * @return ResponseEntity<EnvelopeResponse < GetAccountHistoryResponseDto>>
	 */
	@Operation(summary = "거래 내역 조회", description = "사용자가 거래 내역을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "계좌 내역 조회 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1002", description = "Account 통신에 실패했습니다.", content = @Content),
	})
	@GetMapping("/history")
	public ResponseEntity<EnvelopeResponse<GetAccountHistoryResponseDto>> inquireAccountHistory(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestParam("account_type") String accountType
	) {
		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetAccountHistoryResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(
					accountHistoryService.inquireAccountHistory(authenticatedMemberArgument.getMemberId(), accountType)
				)
				.build());
	}
}
