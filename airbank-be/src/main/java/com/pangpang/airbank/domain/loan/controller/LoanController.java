package com.pangpang.airbank.domain.loan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.loan.dto.GetLoanResponseDto;
import com.pangpang.airbank.domain.loan.service.LoanService;
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
@RequestMapping("/loans")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "loans", description = "땡겨쓰기 API")
public class LoanController {

	private final LoanService loanService;

	/**
	 *  땡겨쓰기 조회
	 *
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
	// @CheckGroup
	@GetMapping()
	public ResponseEntity<EnvelopeResponse<GetLoanResponseDto>> getLoan(@RequestParam("group_id") Long groupId) {
		AuthenticatedMemberArgument member = new AuthenticatedMemberArgument(2L);

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetLoanResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(loanService.getLoan(member.getMemberId(), groupId))
				.build());
	}
}
