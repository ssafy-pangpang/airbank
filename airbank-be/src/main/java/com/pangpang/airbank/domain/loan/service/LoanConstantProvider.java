package com.pangpang.airbank.domain.loan.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "loan.constant")
public class LoanConstantProvider {
	private final Integer loanThreshold;
}
