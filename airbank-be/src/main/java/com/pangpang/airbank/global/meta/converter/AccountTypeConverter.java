package com.pangpang.airbank.global.meta.converter;

import com.pangpang.airbank.global.meta.domain.AccountType;

import jakarta.persistence.AttributeConverter;

public class AccountTypeConverter implements AttributeConverter<AccountType, String> {

	@Override
	public String convertToDatabaseColumn(AccountType attribute) {
		return attribute.getName();
	}

	@Override
	public AccountType convertToEntityAttribute(String dbData) {
		return AccountType.ofName(dbData);
	}
}
