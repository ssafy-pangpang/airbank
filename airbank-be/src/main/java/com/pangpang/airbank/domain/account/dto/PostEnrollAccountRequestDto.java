package com.pangpang.airbank.domain.account.dto;

import com.pangpang.airbank.global.meta.domain.BankCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostEnrollAccountRequestDto {
	private String bankCode;
	private String accountNumber;

	public static PostEnrollAccountRequestDto fromVirtualAccount(String accountNumber) {
		return PostEnrollAccountRequestDto.builder()
			.bankCode(BankCode.NONGHYUP_BANK.getCode())
			.accountNumber(accountNumber)
			.build();
	}
}
