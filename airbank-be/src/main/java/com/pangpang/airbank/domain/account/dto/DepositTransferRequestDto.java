package com.pangpang.airbank.domain.account.dto;

import com.pangpang.airbank.global.meta.domain.BankCode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepositTransferRequestDto {
	private String bnod;
	private String acno;
	private String tram;
	private String mractOtlt;

	public static DepositTransferRequestDto of(TransferRequestDto transferRequestDto, String transactionIdentifier) {
		return DepositTransferRequestDto.builder()
			.bnod(BankCode.NONGHYUP_BANK.getCode())
			.acno(transferRequestDto.getReceiverAccount().getAccountNumber())
			.tram(transferRequestDto.getAmount().toString())
			.mractOtlt(transactionIdentifier)
			.build();
	}
}
