package com.pangpang.airbank.global.common.api.nh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class GetInquireBalanceRequestDto {
	private CommonHeaderDto header;
	private String finAcno;

	public static GetInquireBalanceRequestDto of(NHApiConstantProvider nhApiConstantProvider, Long isTuno,
		String finAcno) {
		return GetInquireBalanceRequestDto.builder()
			.header(
				CommonHeaderDto.of(nhApiConstantProvider, isTuno).toBuilder()
					.apiNm("InquireBalance")
					.apiSvcCd("ReceivedTransferA")
					.build()
			)
			.finAcno(finAcno)
			.build();
	}
}
