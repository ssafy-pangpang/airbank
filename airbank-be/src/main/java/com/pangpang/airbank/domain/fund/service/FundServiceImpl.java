package com.pangpang.airbank.domain.fund.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.pangpang.airbank.domain.fund.domain.Tax;
import com.pangpang.airbank.domain.fund.dto.GetTaxResponseDto;
import com.pangpang.airbank.domain.fund.repository.TaxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {
	private final TaxRepository taxRepository;

	/**
	 *  현재 세금 현황 조회
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetTaxResponseDto
	 * @see TaxRepository
	 */
	@Override
	public GetTaxResponseDto getTax(Long memberId, Long groupId) {
		LocalDate endDate = LocalDate.now();
		// 밀린 세금의 기준 날짜
		endDate = endDate.withDayOfMonth(1);

		Long overdueAmount = taxRepository.findOverAmountsByGroupIdAndActivatedFalseAndExpiredAtLessThan(groupId,
			endDate);
		Tax tax = taxRepository.findByGroupIdAndActivatedFalseAndExpiredAtGreaterThanEqual(groupId, endDate)
			.orElseGet(Tax::new);

		return GetTaxResponseDto.of(tax, overdueAmount);
	}
}
