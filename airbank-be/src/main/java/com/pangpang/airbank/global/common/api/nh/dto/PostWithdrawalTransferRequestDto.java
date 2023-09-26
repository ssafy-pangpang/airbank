package com.pangpang.airbank.global.common.api.nh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pangpang.airbank.domain.account.dto.WithdrawalTransferRequestDto;
import com.pangpang.airbank.global.common.api.nh.NHApiConstantProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.PascalCaseStrategy.class)
public class PostWithdrawalTransferRequestDto {
	private CommonHeaderDto header;
	private String finAcno;
	private String tram;
	private String dractOtlt;

	public static PostWithdrawalTransferRequestDto of(NHApiConstantProvider nhApiConstantProvider, Long isTuno,
		WithdrawalTransferRequestDto withdrawalTransferRequestDto) {
		return PostWithdrawalTransferRequestDto.builder()
			.header(
				CommonHeaderDto.of(nhApiConstantProvider, isTuno).toBuilder()
					.apiNm("DrawingTransfer")
					.apiSvcCd("DrawingTransferA")
					.build()
			)
			.finAcno(withdrawalTransferRequestDto.getFinAcn())
			.tram(withdrawalTransferRequestDto.getTram())
			.dractOtlt(withdrawalTransferRequestDto.getDractOtlt())
			.build();
	}
}
