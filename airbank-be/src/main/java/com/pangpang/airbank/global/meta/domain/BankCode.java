package com.pangpang.airbank.global.meta.domain;

import java.util.Arrays;

import com.pangpang.airbank.global.error.exception.MetaException;
import com.pangpang.airbank.global.error.info.MetaErrorInfo;

import lombok.Getter;

@Getter
public enum BankCode {
	NONGHYUP_BANK(1, "농협은행", "011"),
	NONGHYUP_FINANCIAL_GROUP(2, "농협상호금융", "012"),
	KOREA_DEVELOPMENT_BANK(3, "산업은행", "002"),
	INDUSTRIAL_BANK(4, "기업은행", "003"),
	KOOKMIN_BANK(5, "국민은행", "004"),
	HANA_BANK(6, "KEB하나은행", "081"),
	WOORI_BANK(7, "우리은행", "020"),
	STANDARD_CHARTERED_BANK(8, "SC제일은행", "023"),
	CITI_BANK(9, "시티은행", "027"),
	DAEGU_BANK(10, "대구은행", "032"),
	KWANGJU_BANK(11, "광주은행", "034"),
	JEJU_BANK(12, "제주은행", "035"),
	JEONBUK_BANK(13, "전북은행", "037"),
	KYONGNAM_BANK(14, "경남은행", "039"),
	SAEMAEUL_GEUMGO(15, "새마을금고", "045"),
	SHINHAN_BANK(16, "신한은횅", "088"),
	KAKAO_BANK(17, "카카오뱅크", "090");

	private final Integer id;
	private final String name;
	private final String code;

	BankCode(Integer id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public static BankCode ofCode(String code) {
		return Arrays.stream(BankCode.values())
			.filter(value -> value.getCode().equals(code))
			.findAny()
			.orElseThrow(() -> new MetaException(MetaErrorInfo.INVALID_METADATA));
	}
}
