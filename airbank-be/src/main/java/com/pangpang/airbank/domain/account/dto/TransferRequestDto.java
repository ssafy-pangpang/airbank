package com.pangpang.airbank.domain.account.dto;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransferRequestDto {
	private Account senderAccount;
	private Account receiverAccount;
	private Long amount;
	private TransactionType type;

	public static TransferRequestDto of(Account senderAccount, Account receiverAccount, Long amount,
		TransactionType type) {
		return TransferRequestDto.builder()
			.senderAccount(senderAccount)
			.receiverAccount(receiverAccount)
			.amount(amount)
			.type(type)
			.build();
	}
}
