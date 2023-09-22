package com.pangpang.airbank.domain.account.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostEnrollAccountRequestDto {
	private String bankCode;
	private String accountNumber;
}
