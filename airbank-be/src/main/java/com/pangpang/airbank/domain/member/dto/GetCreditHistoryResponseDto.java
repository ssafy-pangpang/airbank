package com.pangpang.airbank.domain.member.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.pangpang.airbank.domain.member.domain.CreditHistory;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.domain.MemberRole;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetCreditHistoryResponseDto {
	private List<CreditHistoryElement> creditHistories;

	public static GetCreditHistoryResponseDto from(List<CreditHistory> creditHistories) {
		return GetCreditHistoryResponseDto.builder()
			.creditHistories(creditHistories.stream()
				.map(creditHistory -> CreditHistoryElement.from(creditHistory))
				.collect(Collectors.toList()))
			.build();
	}
}
