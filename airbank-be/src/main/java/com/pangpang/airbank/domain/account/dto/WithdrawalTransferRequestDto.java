package com.pangpang.airbank.domain.account.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WithdrawalTransferRequestDto {
	private String finAcn;
	private String tram;
	private String dractOtlt;

	public static WithdrawalTransferRequestDto of(TransferRequestDto transferRequestDto, String transactionIdentifier) {
		return WithdrawalTransferRequestDto.builder()
			.finAcn(transferRequestDto.getSenderAccount().getFinAccountNumber())
			.tram(transferRequestDto.getAmount().toString())
			.dractOtlt(transactionIdentifier)
			.build();
	}
}
