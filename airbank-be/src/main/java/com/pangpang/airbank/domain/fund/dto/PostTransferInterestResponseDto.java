package com.pangpang.airbank.domain.fund.dto;

import com.pangpang.airbank.domain.account.dto.TransferResponseDto;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostTransferInterestResponseDto {
	private Long amount;
	private String transactionType;

	public static PostTransferInterestResponseDto of(TransferResponseDto transferResponseDto, TransactionType type) {
		return PostTransferInterestResponseDto.builder()
			.amount(transferResponseDto.getAmount())
			.transactionType(type.getName())
			.build();
	}
}