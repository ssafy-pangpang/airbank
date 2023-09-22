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
public class GetCheckFinAccountRequestDto {
	private CommonHeaderDto header;
	private String rgno;
	private String brdtBrno;

	public static GetCheckFinAccountRequestDto of(NHApiConstantProvider nhApiConstantProvider, Long isTuno,
		String rgno) {
		return GetCheckFinAccountRequestDto.builder()
			.header(
				CommonHeaderDto.of(nhApiConstantProvider, isTuno).toBuilder()
					.apiNm("CheckOpenFinAccountDirect")
					.apiSvcCd("DrawingTransferA")
					.build()
			)
			.rgno(rgno)
			.brdtBrno(nhApiConstantProvider.getBirth())
			.build();
	}
}
