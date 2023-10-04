package com.pangpang.airbank.global.common.api.nh.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

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
public class CommonHeaderDto {
	private String apiNm;
	private String tsymd;
	private String trtm;
	private String iscd;
	private String fintechApsno;
	private String apiSvcCd;
	private String isTuno;
	private String accessToken;
	private String rpcd;
	private String rsms;
	private String rgno;

	public static CommonHeaderDto of(NHApiConstantProvider nhApiConstantProvider, Long isTuno) {
		SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
		Date now = new Date();

		return CommonHeaderDto.builder()
			.tsymd(dayFormatter.format(now))
			.trtm(timeFormatter.format(now))
			.iscd(nhApiConstantProvider.getIscd())
			.fintechApsno("001")
			.isTuno(isTuno.toString())
			.accessToken(nhApiConstantProvider.getAccessToken())
			.build();
	}

	@Override
	public String toString() {
		return "CommonHeaderDto{" +
			"apiNm='" + apiNm + '\'' +
			", tsymd='" + tsymd + '\'' +
			", trtm='" + trtm + '\'' +
			", iscd='" + iscd + '\'' +
			", fintechApsno='" + fintechApsno + '\'' +
			", apiSvcCd='" + apiSvcCd + '\'' +
			", isTuno='" + isTuno + '\'' +
			", accessToken='" + accessToken + '\'' +
			", rpcd='" + rpcd + '\'' +
			", rsms='" + rsms + '\'' +
			", rgno='" + rgno + '\'' +
			'}';
	}
}
