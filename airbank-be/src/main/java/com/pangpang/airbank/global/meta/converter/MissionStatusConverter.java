package com.pangpang.airbank.global.meta.converter;

import com.pangpang.airbank.global.meta.domain.MissionStatus;

import jakarta.persistence.AttributeConverter;

public class MissionStatusConverter implements AttributeConverter<MissionStatus, String> {
	@Override
	public String convertToDatabaseColumn(MissionStatus attribute) {
		return attribute.getName();
	}

	@Override
	public MissionStatus convertToEntityAttribute(String dbData) {
		return MissionStatus.ofName(dbData);
	}
}
