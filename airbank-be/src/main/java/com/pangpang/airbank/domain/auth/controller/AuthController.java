package com.pangpang.airbank.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.auth.dto.GetLoginRequestDto;
import com.pangpang.airbank.domain.auth.dto.GetLoginResponseDto;
import com.pangpang.airbank.domain.auth.dto.GetLogoutResponseDto;
import com.pangpang.airbank.domain.auth.service.AuthService;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.dto.GetLoginMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PostLoginRequestDto;
import com.pangpang.airbank.domain.member.service.MemberService;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.Authentication;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final MemberService memberService;

	/**
	 *  auth별 로그인 창 출력
	 *
	 * @param HttpServletResponse response
	 * @return 카카오 로그인 창으로 redirect
	 */
	@GetMapping("/login")
	public void login(HttpServletResponse response) {
		authService.sendRedirectUrl(response);
	}

	/**
	 *  인가 코드로 콜백되어 토큰 발급 및 사용자 정보 조회 후 회원가입 및 로그인 처리
	 *
	 * @param HttpServletRequest request
	 *        GetLoginRequestDto getLoginRequestDto
	 * @return 현재 로그인한 사용자의 이름과 휴대폰번호
	 */
	@GetMapping("/callback")
	public ResponseEntity<EnvelopeResponse<GetLoginResponseDto>> login(HttpServletRequest request,
		@RequestParam("code") GetLoginRequestDto getLoginRequestDto) {
		HttpSession session = request.getSession();

		String accessToken = authService.getKakaoAccessToken(getLoginRequestDto.getCode()).getAccessToken();
		PostLoginRequestDto postLoginRequestDto = authService.getKakaoProfile(accessToken);
		GetLoginMemberResponseDto getLoginMemberResponseDto
			= memberService.getMemberByOauthIdentifier(postLoginRequestDto);
		session.setAttribute("memberId", getLoginMemberResponseDto.getId());
		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetLoginResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(getLoginMemberResponseDto.getGetLoginResponseDto())
				.build());
	}

	/**
	 *  사용자 로그아웃 요청 처리
	 * 
	 * @param HttpServletRequest request
	 *        AuthenticatedMemberArgument authenticatedMemberArgument
	 */
	@GetMapping("/logout")
	public ResponseEntity<EnvelopeResponse<GetLogoutResponseDto>> logout(HttpServletRequest request,
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument) {
		String oauthIdentifier = memberService.getMemberOauthIdentifier(authenticatedMemberArgument.getMemberId());
		authService.getKakaoLogout(oauthIdentifier);
		request.getSession().invalidate();

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetLogoutResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(memberService.getMemberName(authenticatedMemberArgument.getMemberId()))
				.build());
	}

}
