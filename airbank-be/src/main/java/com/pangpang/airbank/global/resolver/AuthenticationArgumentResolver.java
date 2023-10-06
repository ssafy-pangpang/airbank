package com.pangpang.airbank.global.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.pangpang.airbank.domain.member.service.MemberService;
import com.pangpang.airbank.global.error.exception.AuthException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.AuthErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
	private final MemberService memberService;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(Authentication.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		HttpSession session = request.getSession();

		Object sessionMemberId = session.getAttribute("memberId");
		if (sessionMemberId == null) {
			throw new AuthException(AuthErrorInfo.UNAUTHENTICATED);
		}

		Long memberId = Long.valueOf(String.valueOf(sessionMemberId));

		if (!memberService.isValidMember(memberId)) {
			throw new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER);
		}
		return new AuthenticatedMemberArgument(memberId);
	}
}
