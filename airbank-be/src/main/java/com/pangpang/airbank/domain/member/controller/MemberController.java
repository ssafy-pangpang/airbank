package com.pangpang.airbank.domain.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberRequestDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberResponseDto;
import com.pangpang.airbank.domain.member.service.MemberService;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.Authentication;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	/**
	 *  사용자 정보 조회
	 * 
	 * @param AuthenticatedMemberArgument authenticatedMemberArgument
	 * @return 사용자 정보
	 * @see AuthenticationArgumentResolver
	 */
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
	 * @param AuthenticatedMemberArgument authenticatedMemberArgument
	 *        PatchMemberRequestDto patchMemberRequestDto
	 * @return 수정된 회원 정보
	 */
	@PatchMapping()
	public ResponseEntity<EnvelopeResponse<PatchMemberResponseDto>> updateMember(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument, @RequestBody PatchMemberRequestDto patchMemberRequestDto) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<PatchMemberResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(memberService.updateMember(authenticatedMemberArgument.getMemberId(), patchMemberRequestDto))
				.build());
	}
}
