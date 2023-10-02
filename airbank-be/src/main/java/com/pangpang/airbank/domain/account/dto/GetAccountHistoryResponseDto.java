package com.pangpang.airbank.domain.account.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAccountHistoryResponseDto {
	List<AccountHistoryElement> accountHistoryElements;

	public static GetAccountHistoryResponseDto from(List<AccountHistoryElement> accountHistoryElements) {
		return GetAccountHistoryResponseDto.builder()
			.accountHistoryElements(accountHistoryElements)
			.build();
	}
}
