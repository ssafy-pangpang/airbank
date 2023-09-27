package com.pangpang.airbank.domain.fund.service;

import com.pangpang.airbank.domain.fund.dto.GetInterestResponseDto;
import com.pangpang.airbank.domain.fund.dto.GetTaxResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusResponseDto;

public interface FundService {
	GetTaxResponseDto getTax(Long memberId, Long groupId);

	GetInterestResponseDto getInterest(Long memberId, Long groupId);

	PostTransferBonusResponseDto transferBonus(PostTransferBonusRequestDto postTransferBonusRequestDto, Long memberId,
		Long groupId);
}
