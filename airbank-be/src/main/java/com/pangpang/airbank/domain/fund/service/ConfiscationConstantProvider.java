package com.pangpang.airbank.domain.fund.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "confiscation.constant")
public class ConfiscationConstantProvider {
	private final Integer confiscationThreshold;
}
