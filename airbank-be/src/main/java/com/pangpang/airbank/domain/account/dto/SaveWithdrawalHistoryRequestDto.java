package com.pangpang.airbank.domain.account.dto;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.domain.TransactionDistinction;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveWithdrawalHistoryRequestDto {
	private Account account;
	private Member transactionPartner;
	private Long amount;
	private TransactionType transactionType;
	private TransactionDistinction transactionDistinction;

	public static SaveWithdrawalHistoryRequestDto from(TransferRequestDto transferRequestDto) {
		return SaveWithdrawalHistoryRequestDto.builder()
			.account(transferRequestDto.getSenderAccount())
			.transactionPartner(transferRequestDto.getReceiverAccount().getMember())
			.amount(transferRequestDto.getAmount())
			.transactionType(transferRequestDto.getType())
			.transactionDistinction(TransactionDistinction.WITHDRAW)
			.build();
	}
}
