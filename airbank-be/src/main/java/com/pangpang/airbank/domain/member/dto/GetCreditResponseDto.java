package com.pangpang.airbank.domain.member.dto;

import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.domain.CreditRating;
import com.pangpang.airbank.global.meta.domain.MemberRole;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetCreditResponseDto {
	private Integer creditRating;

	public GetCreditResponseDto(Integer creditRating) {
		this.creditRating = creditRating;
	}
}
