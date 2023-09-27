package com.pangpang.airbank.domain.auth.service;

import org.springframework.stereotype.Service;

import com.pangpang.airbank.global.error.exception.AuthException;
import com.pangpang.airbank.global.error.info.AuthErrorInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	/**
	 *  oauth 식별자 검증
	 *
	 * @param oauthIdentifier validateOauthIdentifier
	 */
	@Override
	public void validateOauthIdentifier(String oauthIdentifier) {
		if (oauthIdentifier == null) {
			new AuthException(AuthErrorInfo.INVALID_OAUTHIDENTIFIER);
		}
	}
}
