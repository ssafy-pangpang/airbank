package com.pangpang.airbank.domain.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.pangpang.airbank.domain.auth.dto.GetKakaoAccessTokenResponseDto;
import com.pangpang.airbank.domain.member.dto.PostLoginRequestDto;
import com.pangpang.airbank.global.error.exception.AuthException;
import com.pangpang.airbank.global.error.info.AuthErrorInfo;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AuthConstantProvider authConstantProvider;
	private static final String KAKAO_URL = "https://kauth.kakao.com/oauth/authorize";
	private static final String KAKAO_AUTH_URI = "https://kauth.kakao.com/oauth/token";
	private static final String KAKAO_API_URI = "https://kapi.kakao.com/v2/user/me";
	private static final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

	/**
	 *  카카오 로그인 창 출력
	 *
	 * @param HttpServletResponse response
	 * @return 카카오 로그인 창으로 redirect
	 */
	@Override
	public void sendRedirectUrl(HttpServletResponse response) {
		try {
			response.sendRedirect(getKakaoUrl());
		} catch (Exception e) {
			throw new AuthException(AuthErrorInfo.AUTH_SERVER_ERROR);
		}
	}

	/**
	 *  카카오 로그인 창 주소 생성
	 * 
	 * @return 카카오 로그인 창 URL
	 */
	private String getKakaoUrl() {
		return new StringBuilder().append(KAKAO_URL).append("?")
			.append("client_id=").append(authConstantProvider.getClientId()).append("&")
			.append("redirect_uri=").append(authConstantProvider.getRedirectUri()).append("&")
			.append("response_type=").append("code").toString();
	}

	/**
	 *  인가코드로 토큰 발급
	 *
	 * @param String code
	 * @return 카카오 AccessToken
	 */
	@Override
	public GetKakaoAccessTokenResponseDto getKakaoAccessToken(String code) {
		if (code == null) {
			throw new AuthException((AuthErrorInfo.INVALID_AUTH_CODE));
		}

		MultiValueMap<String, String> params = setAccessTokenParameters(code);
		try {
			return WebClient.create()
				.post()
				.uri(KAKAO_AUTH_URI)
				.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
				.bodyValue(params)
				.retrieve()
				.bodyToMono(GetKakaoAccessTokenResponseDto.class)
				.block();
		} catch (Exception e) {
			throw new AuthException(AuthErrorInfo.AUTH_SERVER_ERROR);
		}
	}

	/**
	 *  토큰 발급에 필요한 파라미터 세팅
	 *
	 * @param String code
	 * @return map 형식으로 세팅된 파라미터
	 */
	private MultiValueMap<String, String> setAccessTokenParameters(String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("grant_type", "authorization_code");
		params.add("client_id", authConstantProvider.getClientId());
		params.add("client_secret", authConstantProvider.getClientSecret());
		params.add("redirect_uri", authConstantProvider.getRedirectUri());
		params.add("code", code);

		return params;
	}

	/**
	 *  토큰으로 사용자 정보 조회
	 *
	 * @param String accessToken
	 * @return 카카오에서 조회된 사용자 정보
	 */
	@Override
	public PostLoginRequestDto getKakaoProfile(String accessToken) {
		try {
			return WebClient.create()
				.post()
				.uri(KAKAO_API_URI)
				.headers(header -> header.setBearerAuth(accessToken))
				.retrieve()
				.bodyToMono(PostLoginRequestDto.class)
				.block();
		} catch (Exception e) {
			throw new AuthException(AuthErrorInfo.AUTH_SERVER_ERROR);
		}
	}

	/**
	 *  카카오 로그아웃
	 *
	 * @param String oauthIdentifier
	 * @return 리턴하는 값 설명
	 */
	@Override
	public String getKakaoLogout(String oauthIdentifier) {
		MultiValueMap<String, String> params = setLogoutParameters(oauthIdentifier);
		try {
			return WebClient.create()
				.post()
				.uri(KAKAO_LOGOUT_URL)
				.header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
				.header("Authorization", "KakaoAK " + authConstantProvider.getServiceAppAdminKey())
				.bodyValue(params)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		} catch (Exception e) {
			throw new AuthException(AuthErrorInfo.AUTH_SERVER_ERROR);
		}
	}

	/**
	 *  로그아웃에 필요한 파라미터 세팅
	 *
	 * @param String oauthIdentifier
	 * @return map 형식으로 세팅된 파라미터
	 */
	private MultiValueMap<String, String> setLogoutParameters(String oauthIdentifier) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("target_id_type", "user_id");
		params.add("target_id", oauthIdentifier);

		return params;
	}
}
