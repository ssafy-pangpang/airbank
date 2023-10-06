package com.pangpang.airbank.domain.account.dto;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.domain.TransactionDistinction;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveDepositHistoryRequestDto {
	private Account account;
	private Member transactionPartner;
	private Long amount;
	private TransactionType transactionType;
	private TransactionDistinction transactionDistinction;

	public static SaveDepositHistoryRequestDto from(TransferRequestDto transferRequestDto) {
		return SaveDepositHistoryRequestDto.builder()
			.account(transferRequestDto.getReceiverAccount())
			.transactionPartner(transferRequestDto.getSenderAccount().getMember())
			.amount(transferRequestDto.getAmount())
			.transactionType(transferRequestDto.getType())
			.transactionDistinction(TransactionDistinction.DEPOSIT)
			.build();
	}
}
