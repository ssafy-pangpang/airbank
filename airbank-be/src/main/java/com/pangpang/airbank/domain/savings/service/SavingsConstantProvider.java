package com.pangpang.airbank.domain.savings.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "savings.constant")
public class SavingsConstantProvider {
	private final Integer failThreshold;
}
