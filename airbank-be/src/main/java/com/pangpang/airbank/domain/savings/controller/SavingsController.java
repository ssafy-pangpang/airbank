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

import com.pangpang.airbank.domain.group.dto.CommonIdResponseDto;
import com.pangpang.airbank.domain.savings.dto.GetCurrentSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchCancelSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PatchCommonSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchConfirmSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostSaveSavingsRequestDto;
import com.pangpang.airbank.domain.savings.service.SavingsService;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import io.swagger.v3.oas.annotations.Operation;
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
		@RequestParam("group_id") Long groupId) {
		AuthenticatedMemberArgument member = new AuthenticatedMemberArgument(2L);

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetCurrentSavingsResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(savingsService.getCurrentSavings(groupId))
				.build());
	}

	/**
	 *  티끌모으기 생성
	 *
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
		@RequestBody PostSaveSavingsRequestDto postSaveSavingsRequestDto) {
		AuthenticatedMemberArgument member = new AuthenticatedMemberArgument(2L);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(EnvelopeResponse.<CommonIdResponseDto>builder()
				.code(HttpStatus.CREATED.value())
				.data(savingsService.saveSavings(member.getMemberId(), postSaveSavingsRequestDto))
				.build());
	}

	/**
	 *  티끌모으기 요청 수락/거절
	 *
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
		@ApiResponse(responseCode = "1306", description = "사용자가 해당 그룹에 속해있지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1804", description = "티끌모으기 수락/거절 권한이 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1805", description = "등록 대기중인 티끌모으기를 찾을 수 없습니다.", content = @Content)
	})
	// @CheckGroup
	@PatchMapping("/confirm")
	public ResponseEntity<EnvelopeResponse<PatchCommonSavingsResponseDto>> confirmEnrollmentSavings(
		@RequestBody PatchConfirmSavingsRequestDto patchConfirmSavingsRequestDto,
		@RequestParam("group_id") Long groupId) {
		AuthenticatedMemberArgument member = new AuthenticatedMemberArgument(1L);

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<PatchCommonSavingsResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(savingsService.confirmEnrollmentSavings(member.getMemberId(), patchConfirmSavingsRequestDto,
					groupId))
				.build());
	}

	/**
	 *  티끌모으기 포기
	 *
	 * @param patchCancelSavingsRequestDto PatchCancelSavingsRequestDto
	 * @return ResponseEntity<EnvelopeResponse < PatchCommonSavingsResponseDto>>
	 * @see SavingsService
	 */
	@Operation(summary = "티끌모으기 포기", description = "진행중인 티끌모으기를 포기하는 API, 자녀만 가능")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "티끌모으기 요청 수락/거절 성공",
			content = @Content(schema = @Schema(implementation = PatchCommonSavingsResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1807", description = "티끌모으기 포기는 자녀만 가능합니다.", content = @Content),
		@ApiResponse(responseCode = "1800", description = "진행중인 티끌모으기를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1808", description = "이미 종료된 티끌모으기 입니다.", content = @Content)
	})
	@PatchMapping("/cancel")
	public ResponseEntity<EnvelopeResponse<PatchCommonSavingsResponseDto>> cancelSavings(
		@RequestBody PatchCancelSavingsRequestDto patchCancelSavingsRequestDto) {
		AuthenticatedMemberArgument member = new AuthenticatedMemberArgument(2L);

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<PatchCommonSavingsResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(savingsService.cancelSavings(member.getMemberId(), patchCancelSavingsRequestDto))
				.build());
	}
}
