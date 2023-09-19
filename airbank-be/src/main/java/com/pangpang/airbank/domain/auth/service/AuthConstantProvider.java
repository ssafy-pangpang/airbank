package com.pangpang.airbank.domain.auth.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "auth.kakao")
public class AuthConstantProvider {
	private final String clientId;
	private final String clientSecret;
	private final String redirectUri;
}
