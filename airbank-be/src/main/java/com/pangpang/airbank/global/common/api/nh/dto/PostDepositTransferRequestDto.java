package com.pangpang.airbank.global.common.api.nh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pangpang.airbank.domain.account.dto.DepositTransferRequestDto;
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
public class PostDepositTransferRequestDto {
	private CommonHeaderDto header;
	private String bncd;
	private String acno;
	private String tram;
	private String mractOtlt;

	public static PostDepositTransferRequestDto of(NHApiConstantProvider nhApiConstantProvider, Long isTuno,
		DepositTransferRequestDto depositTransferRequestDto) {
		return PostDepositTransferRequestDto.builder()
			.header(
				CommonHeaderDto.of(nhApiConstantProvider, isTuno).toBuilder()
					.apiNm("ReceivedTransferAccountNumber")
					.apiSvcCd("ReceivedTransferA")
					.build()
			)
			.bncd(depositTransferRequestDto.getBnod())
			.acno(depositTransferRequestDto.getAcno())
			.tram(depositTransferRequestDto.getTram())
			.mractOtlt(depositTransferRequestDto.getMractOtlt())
			.build();
	}
}
