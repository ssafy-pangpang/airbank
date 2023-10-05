package com.pangpang.airbank.domain.loan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.loan.dto.GetLoanResponseDto;
import com.pangpang.airbank.domain.loan.dto.PostCommonLoanRequestDto;
import com.pangpang.airbank.domain.loan.dto.PostRepaidLoanResponseDto;
import com.pangpang.airbank.domain.loan.service.LoanService;
import com.pangpang.airbank.global.aop.CheckGroup;
import com.pangpang.airbank.global.common.response.CommonAmountResponseDto;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.Authentication;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "loans", description = "땡겨쓰기 API")
public class LoanController {

	private final LoanService loanService;

	/**
	 *  땡겨쓰기 조회
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < GetLoanResponseDto>>
	 * @see LoanService
	 */
	@Operation(summary = "땡겨쓰기 조회", description = "땡겨쓰기(한도, 땡겨쓴 금액)를 조회하는 메소드, 부모와 자녀가 조회 가능하다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "땡겨쓰기 조회 성공",
			content = @Content(schema = @Schema(implementation = GetLoanResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1306", description = "사용자가 해당 그룹에 속해있지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1200", description = "자금 관리를 찾을 수 없습니다.", content = @Content)
	})
	@CheckGroup
	@GetMapping()
	public ResponseEntity<EnvelopeResponse<GetLoanResponseDto>> getLoan(
		@Parameter(hidden = true) @Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestParam("group_id") Long groupId) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetLoanResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(loanService.getLoan(authenticatedMemberArgument.getMemberId(), groupId))
				.build());
	}

	/**
	 *  땡겨쓰기 땡기기
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param postCommonLoanRequestDto PostCommonLoanRequestDto
	 * @return ResponseEntity<EnvelopeResponse < CommonAmountResponseDto>>
	 * @see LoanService
	 */
	@Operation(summary = "땡겨쓰기 땡기기", description = "땡겨쓰기 가상계좌에서 자녀 계좌로 입금하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "땡겨쓰기 땡기기 성공",
			content = @Content(schema = @Schema(implementation = CommonAmountResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1401", description = "신용등급이 낮습니다.", content = @Content),
		@ApiResponse(responseCode = "1400", description = "땡겨쓰기는 자녀만 사용할 수 있습니다.", content = @Content),
		@ApiResponse(responseCode = "1006", description = "등록된 땡겨쓰기 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1303", description = "등록중인 그룹이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1200", description = "자금 관리를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1402", description = "땡겨쓰기 계좌 잔액이 부족합니다.", content = @Content),
	})
	@PostMapping()
	public ResponseEntity<EnvelopeResponse<CommonAmountResponseDto>> withdrawLoan(
		@Parameter(hidden = true) @Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostCommonLoanRequestDto postCommonLoanRequestDto) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<CommonAmountResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(loanService.withdrawLoan(authenticatedMemberArgument.getMemberId(), postCommonLoanRequestDto))
				.build());
	}

	/**
	 *  땡겨쓰기 중도 상환
	 *
	 * @param postCommonLoanRequestDto PostCommonLoanRequestDto
	 * @return ResponseEntity<EnvelopeResponse < PostRepaidLoanResponseDto>>
	 * @see LoanService
	 */
	@Operation(summary = "땡겨쓰기 중도 상환", description = "자녀 계좌에서 땡겨쓰기 가상 계좌로 입금하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "땡겨쓰기 중도 상환 성공",
			content = @Content(schema = @Schema(implementation = PostRepaidLoanResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1303", description = "등록된 그룹이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1403", description = "이자가 상환되지 않았습니다.", content = @Content),
		@ApiResponse(responseCode = "1006", description = "등록된 땡겨쓰기 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1200", description = "자금 관리를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1404", description = "상환금액이 땡겨쓰기 한도를 초과했습니다.", content = @Content),
	})
	@PostMapping("/repaid")
	public ResponseEntity<EnvelopeResponse<PostRepaidLoanResponseDto>> repaidLoan(
		@Parameter(hidden = true) @Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostCommonLoanRequestDto postCommonLoanRequestDto) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<PostRepaidLoanResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(loanService.repaidLoan(authenticatedMemberArgument.getMemberId(), postCommonLoanRequestDto))
				.build());
	}

	/**
	 *  이자 생성 cron
	 *
	 * @see LoanService
	 */
	@Operation(summary = "이자 생성 Cron", description = "이자 생성 Cron")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "이자 생성 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
	})
	@GetMapping("/interest-cron")
	public ResponseEntity<EnvelopeResponse<Void>> createInterest() {
		loanService.createInterestByCron();

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.data(null)
				.build());
	}
}
