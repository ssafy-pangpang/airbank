package com.pangpang.airbank.global.meta.converter;

import com.pangpang.airbank.global.meta.domain.MemberRole;

import jakarta.persistence.AttributeConverter;

public class MemberRoleConverter implements AttributeConverter<MemberRole, String> {
	@Override
	public String convertToDatabaseColumn(MemberRole attribute) {
		return attribute.getName();
	}

	@Override
	public MemberRole convertToEntityAttribute(String dbData) {
		return MemberRole.ofName(dbData);
	}
}
