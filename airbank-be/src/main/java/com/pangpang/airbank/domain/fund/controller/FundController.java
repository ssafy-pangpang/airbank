package com.pangpang.airbank.domain.fund.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.fund.dto.GetConfiscationResponseDto;
import com.pangpang.airbank.domain.fund.dto.GetInterestResponseDto;
import com.pangpang.airbank.domain.fund.dto.GetTaxResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusResponseDto;
import com.pangpang.airbank.domain.fund.service.FundService;
import com.pangpang.airbank.global.aop.CheckGroup;
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
 *  자금에 대한 Controller
 */
@RestController
@RequestMapping("/funds")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "funds", description = "자금 관리하는 API")
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
	@Operation(summary = "현재 세금 현황 조회", description = "사용자의 세금 현황을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "세금 현황 조회 성공",
			content = @Content(schema = @Schema(implementation = GetTaxResponseDto.class))),
	})
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

	/**
	 *  이자 조회
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < GetInterestResponseDto>>
	 * @see FundService
	 */
	@Operation(summary = "현재 이자 현황 조회", description = "사용자의 이자 현황을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이자 현황 조회 성공",
			content = @Content(schema = @Schema(implementation = GetInterestResponseDto.class))),
	})
	@CheckGroup
	@GetMapping("/interest")
	public ResponseEntity<EnvelopeResponse<GetInterestResponseDto>> getInterest(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestParam("group_id") Long groupId) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<GetInterestResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(
					fundService.getInterest(authenticatedMemberArgument.getMemberId(), groupId)
				)
				.build());
	}

	/**
	 * 보너스 송금
	 *
	 * @param postTransferBonusRequestDto
	 * @param authenticatedMemberArgument
	 * @param groupId
	 * @return ResponseEntity<EnvelopeResponse < PostTransferBonusResponseDto>>
	 */
	@CheckGroup
	@PostMapping("/bonus")
	public ResponseEntity<EnvelopeResponse<PostTransferBonusResponseDto>> transferBonus(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostTransferBonusRequestDto postTransferBonusRequestDto,
		@RequestParam("group_id") Long groupId) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<PostTransferBonusResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(
					fundService.transferBonus(postTransferBonusRequestDto, authenticatedMemberArgument.getMemberId(),
						groupId)
				)
				.build());
	}

	/**
	 *  압류 조회
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < GetConfiscationResponseDto>>
	 * @see FundService
	 */
	@CheckGroup
	@GetMapping("/confiscation")
	public ResponseEntity<EnvelopeResponse<GetConfiscationResponseDto>> getConfiscation(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestParam("group_id") Long groupId) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<GetConfiscationResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(fundService.getConfiscation((groupId)))
				.build());
	}
}
