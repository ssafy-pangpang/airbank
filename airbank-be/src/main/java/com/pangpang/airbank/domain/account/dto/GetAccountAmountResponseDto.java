package com.pangpang.airbank.domain.account.dto;

import com.pangpang.airbank.global.common.api.nh.dto.GetInquireBalanceResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAccountAmountResponseDto {
	Long amount;

	public static GetAccountAmountResponseDto from(GetInquireBalanceResponseDto getInquireBalanceResponseDto) {
		return GetAccountAmountResponseDto.builder()
			.amount(Long.parseLong(getInquireBalanceResponseDto.getRlpmAbamt()))
			.build();
	}
}
