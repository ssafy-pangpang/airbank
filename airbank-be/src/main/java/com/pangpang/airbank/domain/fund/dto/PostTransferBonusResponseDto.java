package com.pangpang.airbank.domain.fund.dto;

import com.pangpang.airbank.domain.account.dto.TransferResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostTransferBonusResponseDto {
	private Long amount;

	public static PostTransferBonusResponseDto from(TransferResponseDto transferResponseDto) {
		return PostTransferBonusResponseDto.builder()
			.amount(transferResponseDto.getAmount())
			.build();
	}
}