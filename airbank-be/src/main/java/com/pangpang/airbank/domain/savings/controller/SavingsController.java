package com.pangpang.airbank.domain.savings.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.savings.dto.GetCurrentSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchCancelSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PatchCommonSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchConfirmSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostRewardSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostSaveSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostTransferSavingsRequestDto;
import com.pangpang.airbank.domain.savings.service.SavingsService;
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
@RequestMapping("/savings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "savings", description = "티끌모으기 API")
public class SavingsController {

	private final SavingsService savingsService;

	/**
	 *  티끌모으기 현재 내역 조회
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < GetCurrentSavingsResponseDto>>
	 * @see SavingsService
	 */
	@Operation(summary = "티끌모으기 현재 내역 조회", description = "현재 진행중인 티끌모으기 정보를 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "티끌모으기 현재 내역 조회 성공",
			content = @Content(schema = @Schema(implementation = GetCurrentSavingsResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1306", description = "사용자가 해당 그룹에 속해있지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1800", description = "진행중인 티끌모으기를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1801", description = "티끌모으기 상품 정보를 찾을 수 없습니다.", content = @Content)
	})
	// @CheckGroup
	@GetMapping("/current")
	public ResponseEntity<EnvelopeResponse<GetCurrentSavingsResponseDto>> getCurrentSavings(
		@Parameter(hidden = true) @Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestParam("group_id") Long groupId) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetCurrentSavingsResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(savingsService.getCurrentSavings(groupId))
				.build());
	}

	/**
	 *  티끌모으기 생성
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param postSaveSavingsRequestDto PostSaveSavingsRequestDto
	 * @return ResponseEntity<EnvelopeResponse < CommonIdResponseDto>>
	 * @see SavingsService
	 */
	@Operation(summary = "티끌모으기 생성", description = "티끌모으기를 생성하는 API, 자녀만 생성할 수 있음.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "티끌모으기 생성 성공",
			content = @Content(schema = @Schema(implementation = CommonIdResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1802", description = "티끌모으기는 자녀만 등록할 수 있습니다.", content = @Content),
		@ApiResponse(responseCode = "1303", description = "등록중인 그룹이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1806", description = "이미 등록 대기중인 티끌모으기가 존재합니다.", content = @Content),
		@ApiResponse(responseCode = "1803", description = "이미 진행중인 티끌모으기 존재합니다.", content = @Content)
	})
	@PostMapping("/item")
	public ResponseEntity<EnvelopeResponse<CommonIdResponseDto>> saveSavings(
		@Parameter(hidden = true) @Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostSaveSavingsRequestDto postSaveSavingsRequestDto) {

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(EnvelopeResponse.<CommonIdResponseDto>builder()
				.code(HttpStatus.CREATED.value())
				.data(savingsService.saveSavings(authenticatedMemberArgument.getMemberId(), postSaveSavingsRequestDto))
				.build());
	}

	/**
	 *  티끌모으기 요청 수락/거절
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param patchConfirmSavingsRequestDto PatchConfirmSavingsRequestDto
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < PatchConfirmSavingsResponseDto>>
	 * @see SavingsService
	 */
	@Operation(summary = "티끌모으기 요청 수락/거절", description = "티끌모으기 요청을 수락 또는 거절하는 API, 부모만 가능.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "티끌모으기 요청 수락/거절 성공",
			content = @Content(schema = @Schema(implementation = PatchCommonSavingsResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1804", description = "티끌모으기 수락/거절 권한이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1805", description = "등록 대기중인 티끌모으기를 찾을 수 없습니다.", content = @Content)
	})
	@PatchMapping("/confirm")
	public ResponseEntity<EnvelopeResponse<PatchCommonSavingsResponseDto>> confirmEnrollmentSavings(
		@Parameter(hidden = true) @Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PatchConfirmSavingsRequestDto patchConfirmSavingsRequestDto,
		@RequestParam("group_id") Long groupId) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<PatchCommonSavingsResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(savingsService.confirmEnrollmentSavings(authenticatedMemberArgument.getMemberId(),
					patchConfirmSavingsRequestDto,
					groupId))
				.build());
	}

	/**
	 *  티끌모으기 포기
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param patchCancelSavingsRequestDto PatchCancelSavingsRequestDto
	 * @return ResponseEntity<EnvelopeResponse < PatchCommonSavingsResponseDto>>
	 * @see SavingsService
	 */
	@Operation(summary = "티끌모으기 포기", description = "진행중인 티끌모으기를 포기하는 API, 자녀만 가능")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "티끌모으기 포기 성공",
			content = @Content(schema = @Schema(implementation = PatchCommonSavingsResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1807", description = "티끌모으기 포기는 자녀만 가능합니다.", content = @Content),
		@ApiResponse(responseCode = "1800", description = "진행중인 티끌모으기를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1808", description = "이미 종료된 티끌모으기 입니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1008", description = "등록된 티끌모으기 계좌가 없습니다.", content = @Content),
	})
	@PatchMapping("/cancel")
	public ResponseEntity<EnvelopeResponse<PatchCommonSavingsResponseDto>> cancelSavings(
		@Parameter(hidden = true) @Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PatchCancelSavingsRequestDto patchCancelSavingsRequestDto) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<PatchCommonSavingsResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(savingsService.cancelSavings(authenticatedMemberArgument.getMemberId(),
					patchCancelSavingsRequestDto))
				.build());
	}

	/**
	 *  티끌모으기 송금
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param postTransferSavingsRequestDto PostTransferSavingsRequestDto
	 * @return ResponseEntity<EnvelopeResponse < CommonAmountResponseDto>>
	 * @see SavingsService
	 */
	@Operation(summary = "티끌모으기 송금", description = "자녀 계좌에서 티끌모으기 가상 계좌로 송금하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "티끌모으기 송금 성공",
			content = @Content(schema = @Schema(implementation = CommonAmountResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1809", description = "티끌모으기 송금은 자녀만 가능합니다.", content = @Content),
		@ApiResponse(responseCode = "1800", description = "진행중인 티끌모으기를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1008", description = "등록된 티끌모으기 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1810", description = "이번달에는 이미 티끌모으기 송금을 완료했습니다.", content = @Content),
	})
	@PostMapping()
	public ResponseEntity<EnvelopeResponse<CommonAmountResponseDto>> transferSavings(
		@Parameter(hidden = true) @Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostTransferSavingsRequestDto postTransferSavingsRequestDto) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<CommonAmountResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(savingsService.transferSavings(authenticatedMemberArgument.getMemberId(),
					postTransferSavingsRequestDto))
				.build());
	}

	/**
	 *  티끌모으기 지원금 송금
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @param postRewardSavingsRequestDto PostRewardSavingsRequestDto
	 * @param groupId Long
	 * @return ResponseEntity<EnvelopeResponse < CommonAmountResponseDto>>
	 * @see SavingsService
	 */
	@Operation(summary = "티끌모으기 지원금 송금", description = "티끌모으기가 완료되었을 때 부모가 승인을 하여 자녀의 계좌로 티끌모으기 금액 + 부모 지원금을 송금하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "티끌모으기 지원금 송금 성공",
			content = @Content(schema = @Schema(implementation = CommonAmountResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1811", description = "티끌모으기 지원금 송금은 부모만 가능합니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1800", description = "진행중인 티끌모으기를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1812", description = "티끌모으기가 아직 완료되지 않았습니다.", content = @Content),
		@ApiResponse(responseCode = "1004", description = "등록된 계좌가 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1008", description = "등록된 티끌모으기 계좌가 없습니다.", content = @Content),
	})
	@PostMapping("/reward")
	public ResponseEntity<EnvelopeResponse<CommonAmountResponseDto>> rewardSavings(
		@Parameter(hidden = true) @Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PostRewardSavingsRequestDto postRewardSavingsRequestDto, @RequestParam("group_id") Long groupId) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<CommonAmountResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(
					savingsService.rewardSavings(authenticatedMemberArgument.getMemberId(), postRewardSavingsRequestDto,
						groupId))
				.build());
	}

	/**
	 *  티끌모으기 납부 지연 확인, Cron 테스트
	 *
	 * @see SavingsService
	 */
	@GetMapping("/delay-cron")
	public ResponseEntity<EnvelopeResponse<Void>> confirmDelaySavings() {
		savingsService.confirmDelaySavings();

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.data(null)
				.build());
	}
}
