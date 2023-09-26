package com.pangpang.airbank.domain.account.dto;

import com.pangpang.airbank.global.meta.domain.BankCode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostEnrollAccountRequestDto {
	private String bankCode;
	private String accountNumber;

	public static PostEnrollAccountRequestDto fromGroup(String accountNumber) {
		return PostEnrollAccountRequestDto.builder()
			.bankCode(BankCode.NONGHYUP_BANK.getCode())
			.accountNumber(accountNumber)
			.build();
	}
}
