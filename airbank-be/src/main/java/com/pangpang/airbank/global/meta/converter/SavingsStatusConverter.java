package com.pangpang.airbank.global.meta.converter;

import com.pangpang.airbank.global.meta.domain.SavingsStatus;

import jakarta.persistence.AttributeConverter;

public class SavingsStatusConverter implements AttributeConverter<SavingsStatus, String> {
	@Override
	public String convertToDatabaseColumn(SavingsStatus attribute) {
		return attribute.getName();
	}

	@Override
	public SavingsStatus convertToEntityAttribute(String dbData) {
		return SavingsStatus.ofName(dbData);
	}
}
