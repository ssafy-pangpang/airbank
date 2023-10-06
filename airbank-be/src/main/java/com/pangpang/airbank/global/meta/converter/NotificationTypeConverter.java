package com.pangpang.airbank.global.meta.converter;

import com.pangpang.airbank.global.meta.domain.NotificationType;

import jakarta.persistence.AttributeConverter;

public class NotificationTypeConverter implements AttributeConverter<NotificationType, String> {
	@Override
	public String convertToDatabaseColumn(NotificationType attribute) {
		return attribute.getName();
	}

	@Override
	public NotificationType convertToEntityAttribute(String dbData) {
		return NotificationType.ofName(dbData);
	}
}
