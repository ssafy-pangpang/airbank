package com.pangpang.airbank.global.common.api.nh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
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
public class GetFinAccountRequestDto {
	private CommonHeaderDto header;
	private String drtrRgyn;
	private String brdtBrno;
	private String bncd;
	private String acno;

	public static GetFinAccountRequestDto of(NHApiConstantProvider nhApiConstantProvider, Long isTuno,
		PostEnrollAccountRequestDto postEnrollAccountRequestDto) {
		return GetFinAccountRequestDto.builder()
			.header(
				CommonHeaderDto.of(nhApiConstantProvider, isTuno).toBuilder()
					.apiNm("OpenFinAccountDirect")
					.apiSvcCd("DrawingTransferA")
					.build()
			)
			.drtrRgyn("Y")
			.brdtBrno(nhApiConstantProvider.getBirth())
			.bncd(postEnrollAccountRequestDto.getBankCode())
			.acno(postEnrollAccountRequestDto.getAccountNumber())
			.build();
	}
}
