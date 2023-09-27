package com.pangpang.airbank.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.auth.dto.GetLogoutResponseDto;
import com.pangpang.airbank.domain.auth.dto.PostLoginRequestDto;
import com.pangpang.airbank.domain.auth.dto.PostLoginResponseDto;
import com.pangpang.airbank.domain.auth.service.AuthService;
import com.pangpang.airbank.domain.member.dto.LoginMemberResponseDto;
import com.pangpang.airbank.domain.member.service.MemberService;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.Authentication;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "auth", description = "사용자 인증을 관리하는 API")
public class AuthController {

	private final AuthService authService;
	private final MemberService memberService;

	/**
	 *  사용자 로그인
	 *
	 * @param request HttpServletRequest
	 *        postLoginRequestDto PostLoginRequestDto
	 * @return 로그인한 사용자 이름, 휴대폰 번호
	 */
	@Operation(summary = "사용자 로그인", description = "카카오 사용자 정보를 바탕으로 에어뱅크 서비스 로그인/회원가입을 진행합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인/회원가입 성공",
			content = @Content(schema = @Schema(implementation = PostLoginResponseDto.class))),
		@ApiResponse(responseCode = "1500", description = "사용자를 찾을 수 없습니다.", content = @Content)
	})
	@PostMapping("/login")
	public ResponseEntity<EnvelopeResponse<PostLoginResponseDto>> login(HttpServletRequest request,
		@RequestBody PostLoginRequestDto postLoginRequestDto) {
		HttpSession session = request.getSession();

		authService.validateOauthIdentifier(postLoginRequestDto.getOauthIdentifier());

		LoginMemberResponseDto loginMemberResponseDto
			= memberService.getMemberByOauthIdentifier(postLoginRequestDto);
		session.setAttribute("memberId", loginMemberResponseDto.getId());

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<PostLoginResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(PostLoginResponseDto.from(loginMemberResponseDto))
				.build());
	}

	/**
	 *  사용자 로그아웃
	 *
	 * @param request HttpServletRequest
	 * @return 로그아웃한 사용자의 이름
	 */
	@GetMapping("/logout")
	public ResponseEntity<EnvelopeResponse<GetLogoutResponseDto>> logout(HttpServletRequest request,
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument) {
		request.getSession().invalidate();

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetLogoutResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(memberService.getMemberName(authenticatedMemberArgument.getMemberId()))
				.build());
	}

}
