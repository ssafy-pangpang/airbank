package com.pangpang.airbank.global.meta.converter;

import com.pangpang.airbank.global.meta.domain.TransactionDistinction;

import jakarta.persistence.AttributeConverter;

public class TransactionDistinctionConverter implements AttributeConverter<TransactionDistinction, String> {
	@Override
	public String convertToDatabaseColumn(TransactionDistinction attribute) {
		return attribute.getName();
	}

	@Override
	public TransactionDistinction convertToEntityAttribute(String dbData) {
		return TransactionDistinction.ofName(dbData);
	}
}
