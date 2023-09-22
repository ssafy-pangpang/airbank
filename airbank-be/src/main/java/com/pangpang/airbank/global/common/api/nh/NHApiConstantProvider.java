package com.pangpang.airbank.global.common.api.nh;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "nhapi")
public class NHApiConstantProvider {
	private final String iscd;
	private final String accessToken;
	private final String birth;
	private final String url;
}
