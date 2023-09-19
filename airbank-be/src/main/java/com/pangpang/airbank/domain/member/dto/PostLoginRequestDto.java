package com.pangpang.airbank.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class PostLoginRequestDto {
	private String id;
	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;

	@Getter
	public class KakaoAccount {
		private Profile profile;

		@Getter
		public class Profile {
			@JsonProperty("profile_image_url")
			private String profileImageUrl;
			@JsonProperty("is_default_image")
			private Boolean isDefaultImage;
		}
	}
}
