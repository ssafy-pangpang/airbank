package com.pangpang.airbank.domain.auth.dto;

import lombok.Getter;

@Getter
public class PostLoginRequestDto {
	private String oauthIdentifier;
	private String imageUrl;
	private Boolean isDefaultImage;
}
