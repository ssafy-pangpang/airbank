package com.pangpang.airbank.domain.fund.service;

import com.pangpang.airbank.domain.fund.dto.GetConfiscationResponseDto;
import com.pangpang.airbank.domain.fund.dto.GetInterestResponseDto;
import com.pangpang.airbank.domain.fund.dto.GetTaxResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferConfiscationRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferConfiscationResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferInterestRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferInterestResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferTaxRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferTaxResponseDto;

public interface FundService {
	GetTaxResponseDto getTax(Long memberId, Long groupId);

	PostTransferTaxResponseDto transferTax(Long memberId, PostTransferTaxRequestDto postTransferTaxRequestDto);

	void createTaxes();

	void refundTaxes();

	void checkNoPaymentTaxes();

	GetInterestResponseDto getInterest(Long memberId, Long groupId);

	PostTransferInterestResponseDto transferInterest(Long memberId,
		PostTransferInterestRequestDto postTransferInterestRequestDto);

	PostTransferBonusResponseDto transferBonus(PostTransferBonusRequestDto postTransferBonusRequestDto, Long memberId,
		Long groupId);

	GetConfiscationResponseDto getConfiscation(Long groupId);

	PostTransferConfiscationResponseDto transferConfiscation(Long memberId,
		PostTransferConfiscationRequestDto postTransferConfiscationRequestDto);

	void confiscateLoan(Long childId, Long groupId);

	void transferAllowanceByCron();

	void confiscateLoanByCron();

	void checkNoPaymentInterests();
}
