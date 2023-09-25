package com.pangpang.airbank.domain.fund.service;

import com.pangpang.airbank.domain.fund.dto.GetTaxResponseDto;

public interface FundService {
	GetTaxResponseDto getTax(Long memberId, Long groupId);
}
