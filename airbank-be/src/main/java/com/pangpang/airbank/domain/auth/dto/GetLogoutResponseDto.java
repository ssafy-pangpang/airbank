package com.pangpang.airbank.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetLogoutResponseDto {
	private String name;

	public GetLogoutResponseDto(String name) {
		this.name = getName(name);
	}

	private String getName(String name) {
		if (name == null) {
			return "";
		}
		return name;
	}
}
