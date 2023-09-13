package com.pangpang.airbank.global.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.pangpang.airbank.global.error.exception.AuthException;
import com.pangpang.airbank.global.error.info.AuthErrorInfo;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMember;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
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
		return new AuthenticatedMember(memberId);
	}
}
