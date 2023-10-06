package com.pangpang.airbank.global.meta.converter;

import com.pangpang.airbank.global.meta.domain.TransactionType;

import jakarta.persistence.AttributeConverter;

public class TransactionTypeConverter implements AttributeConverter<TransactionType, String> {
	@Override
	public String convertToDatabaseColumn(TransactionType attribute) {
		return attribute.getName();
	}

	@Override
	public TransactionType convertToEntityAttribute(String dbData) {
		return TransactionType.ofName(dbData);
	}
}
