package com.pangpang.airbank.domain.savings.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSaveSavingsRequestDto {
	private String name;
	private Long amount;
	private Integer month;
	private Long parentsAmount;
	private String imageUrl;
}
