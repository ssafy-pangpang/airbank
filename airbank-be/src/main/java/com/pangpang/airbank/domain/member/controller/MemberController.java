package com.pangpang.airbank.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.member.dto.GetCreditHistoryResponseDto;
import com.pangpang.airbank.domain.member.dto.GetCreditResponseDto;
import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberRequestDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberResponseDto;
import com.pangpang.airbank.domain.member.service.MemberService;
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

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "members", description = "사용자 정보를 관리하는 API")
public class MemberController {

	private final MemberService memberService;

	/**
	 *  사용자 정보 조회
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 * @return 사용자 정보
	 * @see com.pangpang.airbank.global.resolver.AuthenticationArgumentResolver
	 */
	@Operation(summary = "사용자 조회", description = "로그인한 사용자의 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 조회 성공",
			content = @Content(schema = @Schema(implementation = GetMemberResponseDto.class))),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content)
	})
	@GetMapping()
	public ResponseEntity<EnvelopeResponse<GetMemberResponseDto>> getMember(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetMemberResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(memberService.getMember(authenticatedMemberArgument.getMemberId()))
				.build());
	}

	/**
	 *  회원정보 수정
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 *        patchMemberRequestDto PatchMemberRequestDto
	 * @return 수정된 회원 정보
	 */
	@Operation(summary = "회원정보 수정", description = "로그인한 사용자의 정보를 수정합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원정보 수정 성공",
			content = @Content(schema = @Schema(implementation = PatchMemberResponseDto.class))),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1502", description = "이미 가입된 휴대폰 번호입니다.", content = @Content)
	})
	@PatchMapping()
	public ResponseEntity<EnvelopeResponse<PatchMemberResponseDto>> updateMember(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestBody PatchMemberRequestDto patchMemberRequestDto) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<PatchMemberResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(memberService.updateMember(authenticatedMemberArgument.getMemberId(), patchMemberRequestDto))
				.build());
	}

	/**
	 *  신용 등급 조회
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 *        groupId Long
	 * @return 신용등급
	 * @see com.pangpang.airbank.global.meta.domain.CreditRating
	 */
	@Operation(summary = "신용등급 조회", description = "현재 부모-자녀 관계에 포함된 자녀의 신용등급을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "신용등급 조회 성공",
			content = @Content(schema = @Schema(implementation = GetCreditResponseDto.class))),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1306", description = "사용자가 해당 그룹에 속해있지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content)
	})
	@CheckGroup
	@GetMapping("/credit")
	public ResponseEntity<EnvelopeResponse<GetCreditResponseDto>> getCredit(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestParam("group_id") Long groupId) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetCreditResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(memberService.getCreditRating(authenticatedMemberArgument.getMemberId(), groupId))
				.build());
	}

	/**
	 *  신용점수 수정 test
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 *        points Integer
	 * @return void
	 */
	@PostMapping("/credit")
	public ResponseEntity<EnvelopeResponse<Void>> updateCreditScore(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestParam("rate") Double rate) {

		memberService.updateCreditScoreByRate(authenticatedMemberArgument.getMemberId(), rate);
		return ResponseEntity.ok()
			.body(EnvelopeResponse.<Void>builder()
				.code(HttpStatus.OK.value())
				.build());
	}

	/**
	 *  신용점수 변동 내역 조회
	 *
	 * @param authenticatedMemberArgument AuthenticatedMemberArgument
	 *        groupId Long
	 * @return 신용점수 변동 내역 리스트
	 * @see com.pangpang.airbank.domain.member.dto.CreditHistoryElement
	 */
	@Operation(summary = "신용점수 변동내역 조회", description = "현재 부모-자녀 관계에 포함된 자녀의 신용점수 변동내역을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "신용점수 변동내역 조회 성공",
			content = @Content(schema = @Schema(implementation = GetCreditHistoryResponseDto.class))),
		@ApiResponse(responseCode = "1100", description = "인증이 유효하지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1306", description = "사용자가 해당 그룹에 속해있지 않습니다.", content = @Content),
		@ApiResponse(responseCode = "1307", description = "그룹을 찾을 수 없습니다.", content = @Content),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content)
	})
	@CheckGroup
	@GetMapping("/credit-history")
	public ResponseEntity<EnvelopeResponse<GetCreditHistoryResponseDto>> getCreditHistory(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument,
		@RequestParam("group_id") Long groupId) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetCreditHistoryResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(memberService.getCreditHistory(authenticatedMemberArgument.getMemberId(), groupId))
				.build());
	}
}
