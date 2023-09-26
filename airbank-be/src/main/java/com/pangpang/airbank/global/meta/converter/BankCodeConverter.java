package com.pangpang.airbank.global.meta.converter;

import com.pangpang.airbank.global.meta.domain.BankCode;

import jakarta.persistence.AttributeConverter;

public class BankCodeConverter implements AttributeConverter<BankCode, String> {
	@Override
	public String convertToDatabaseColumn(BankCode attribute) {
		return attribute.getCode();
	}

	@Override
	public BankCode convertToEntityAttribute(String dbData) {
		return BankCode.ofCode(dbData);
	}
}
