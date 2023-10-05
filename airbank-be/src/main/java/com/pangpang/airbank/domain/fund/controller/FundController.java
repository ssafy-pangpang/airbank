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
import com.pangpang.airbank.domain.fund.dto.PostTransferConfiscationRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferConfiscationResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferInterestRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferInterestResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferTaxRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferTaxResponseDto;
import com.pangpang.airbank.domain.fund.service.FundService;
import com.pangpang.airbank.global.aop.CheckGroup;
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
	 * 세금의 총 금액을 납부하는 기능
	 *
	 * @param authenticatedMemberArgument
	 * @param postTransferTaxRequestDto
	 * @return ResponseEntity<EnvelopeResponse < PostTransferTaxResponseDto>>
	 */
	@Operation(summary = "세금 송금", description = "사용자의 전체 세금을 냅니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "세금 현황 조회 성공",
			content = @Content(schema = @Schema(implementation = PostTransferTaxResponseDto.class))),
		@ApiResponse(responseCode = "1000", description = "NH API 서버와의 통신에 실패했습니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1204", description = "송금된 금액이 지정 금액과 다릅니다.", content = @Content),
		@ApiResponse(responseCode = "1205", description = "송금될 금액이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
	})
	@PostMapping("/tax")
	public ResponseEntity<EnvelopeResponse<PostTransferTaxResponseDto>> transferTax(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostTransferTaxRequestDto postTransferTaxRequestDto) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<PostTransferTaxResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(
					fundService.transferTax(authenticatedMemberArgument.getMemberId(), postTransferTaxRequestDto)
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
	 * 이자 전체 상환
	 *
	 * @param authenticatedMemberArgument
	 * @param postTransferInterestRequestDto
	 * @return ResponseEntity<EnvelopeResponse < PostTransferInterestResponseDto>>
	 */
	@Operation(summary = "이자 상환", description = "사용자의 전체 이자를 상환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "이자 상환 성공",
			content = @Content(schema = @Schema(implementation = PostTransferTaxResponseDto.class))),
		@ApiResponse(responseCode = "1000", description = "NH API 서버와의 통신에 실패했습니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1204", description = "송금된 금액이 지정 금액과 다릅니다.", content = @Content),
		@ApiResponse(responseCode = "1205", description = "송금될 금액이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
	})
	@PostMapping("/interest")
	public ResponseEntity<EnvelopeResponse<PostTransferInterestResponseDto>> transferInterest(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostTransferInterestRequestDto postTransferInterestRequestDto) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<PostTransferInterestResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(
					fundService.transferInterest(authenticatedMemberArgument.getMemberId(),
						postTransferInterestRequestDto)
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
	@Operation(summary = "보너스 송금", description = "보너스를 송금합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "보너스 송금 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1000", description = "NH API 서버와의 통신에 실패했습니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1204", description = "송금된 금액이 지정 금액과 다릅니다.", content = @Content),
		@ApiResponse(responseCode = "1205", description = "송금될 금액이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
	})
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
	@Operation(summary = "현재 압류 현황 조회", description = "사용자의 압류 현황을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "압류 현황 조회 성공",
			content = @Content(schema = @Schema(implementation = GetConfiscationResponseDto.class))),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1306", description = "사용자가 해당 그룹에 속해있지 않습니다.", content = @Content),
	})
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

	/**
	 *  변제금 송금
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param postTransferConfiscationRequestDto PostTransferConfiscationRequestDto
	 * @return ResponseEntity<EnvelopeResponse < PostTransferConfiscationResponseDto>>
	 * @see FundService
	 */
	@Operation(summary = "변제금 송금", description = "압류금의 일부를 상환합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "변제금 송금 성공",
			content = @Content(schema = @Schema(implementation = PostTransferConfiscationResponseDto.class))),
		@ApiResponse(responseCode = "1000", description = "NH API 서버와의 통신에 실패했습니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1200", description = "자금 관리를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1205", description = "송금될 금액이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1206", description = "변제금을 송금할 권한이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1207", description = "압류중인 재산을 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1208", description = "상환할 수 있는 변제금을 초과했습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content)
	})
	@PostMapping("/confiscation")
	public ResponseEntity<EnvelopeResponse<PostTransferConfiscationResponseDto>> transferConfiscation(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostTransferConfiscationRequestDto postTransferConfiscationRequestDto) {

		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<PostTransferConfiscationResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(fundService.transferConfiscation(authenticatedMemberArgument.getMemberId(),
					postTransferConfiscationRequestDto))
				.build());
	}

	/**
	 *  신용등급이 7등급 이하인 경우 압류하는 메소드, Cron
	 *
	 * @see FundService
	 */
	@Operation(summary = "신용등급이 7등급 이하인 경우 압류하는 메소드 Cron", description = "신용등급이 7등급 이하인 경우 압류하는 메소드 Cron")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "신용등급이 7등급 이하인 경우 압류하는 메소드 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
	})
	@GetMapping("/confiscation-cron")
	public ResponseEntity<EnvelopeResponse<Void>> confiscateLoan() {
		fundService.confiscateLoanByCron();

		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.build());
	}

	/**
	 *  용돈 자동이체 cron
	 *
	 * @see FundService
	 */
	@Operation(summary = "용돈 자동 이체 Cron", description = "용돈 자동 이체 Cron")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "용돈 자동 이체 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1000", description = "NH API 서버와의 통신에 실패했습니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1205", description = "송금될 금액이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
	})
	@GetMapping("/allowance-cron")
	public ResponseEntity<EnvelopeResponse<Void>> transferAllowance() {
		fundService.transferAllowanceByCron();
		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.build());
	}

	/**
	 * 세금 생성 cron
	 * 저번 달 기준으로 세금 생성하고 이미 생성된 세금 있으면 생성 안됨
	 * @return ResponseEntity<EnvelopeResponse < Void>>
	 */
	@Operation(summary = "세금 생성 Cron", description = "세금 생성 Cron")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "세금 생성 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
	})
	@GetMapping("/tax-cron")
	public ResponseEntity<EnvelopeResponse<Void>> createTax() {
		fundService.createTaxes();
		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.build());
	}

	/**
	 * 세금 환급 cron
	 * 저번 달 세금 납부 시, 신용등급에 따라 환급
	 * @return ResponseEntity<EnvelopeResponse < Void>>
	 */
	@Operation(summary = "세금 환급 Cron", description = "세금 환급 Cron")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "세금 생성 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1000", description = "NH API 서버와의 통신에 실패했습니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1205", description = "송금될 금액이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
	})
	@GetMapping("/tax-refund-cron")
	public ResponseEntity<EnvelopeResponse<Void>> transferRefundTax() {
		fundService.refundTaxes();
		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.build());
	}

	/**
	 * 세금 미납 확인 cron
	 * 저번 달 세금 미납시, 신용점수 하락 및 알림
	 * @return ResponseEntity<EnvelopeResponse < Void>>
	 */
	@Operation(summary = "세금 미납 확인 Cron", description = "세금 미납 확인 Cron")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "세금 미납 확인 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
	})
	@GetMapping("/tax-pay-check-cron")
	public ResponseEntity<EnvelopeResponse<Void>> checkNoPaymentTaxes() {
		fundService.checkNoPaymentTaxes();
		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.build());
	}

	/**
	 * 이자 미납 확인 cron
	 * 저번 달 이자 미납 시, 신용점수 하락 및 알림
	 * @return ResponseEntity<EnvelopeResponse < Void>>
	 */
	@Operation(summary = "이자 미납 확인 Cron", description = "이자 미납 확인 Cron")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "이자 미납 확인 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
	})
	@GetMapping("/interest-pay-check-cron")
	public ResponseEntity<EnvelopeResponse<Void>> checkNoPaymentInterests() {
		fundService.checkNoPaymentInterests();
		return ResponseEntity.status(HttpStatus.OK)
			.body(EnvelopeResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.build());
	}
}
