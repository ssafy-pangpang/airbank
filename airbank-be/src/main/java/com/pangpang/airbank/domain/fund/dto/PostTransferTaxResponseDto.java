package com.pangpang.airbank.domain.fund.dto;

import com.pangpang.airbank.domain.account.dto.TransferResponseDto;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostTransferTaxResponseDto {
	private Long amount;
	private String transactionType;

	public static PostTransferTaxResponseDto of(TransferResponseDto transferResponseDto, TransactionType type) {
		return PostTransferTaxResponseDto.builder()
			.amount(transferResponseDto.getAmount())
			.transactionType(type.getName())
			.build();
	}
}