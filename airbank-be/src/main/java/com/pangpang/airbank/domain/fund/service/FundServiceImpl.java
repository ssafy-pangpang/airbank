package com.pangpang.airbank.domain.fund.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.pangpang.airbank.domain.fund.domain.Interest;
import com.pangpang.airbank.domain.fund.domain.Tax;
import com.pangpang.airbank.domain.fund.dto.GetInterestResponseDto;
import com.pangpang.airbank.domain.fund.dto.GetTaxResponseDto;
import com.pangpang.airbank.domain.fund.repository.InterestRepository;
import com.pangpang.airbank.domain.fund.repository.TaxRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {
	private final TaxRepository taxRepository;
	private final InterestRepository interestRepository;

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

	/**
	 *  이자 조회
	 *  1. 오늘 기준으로 이자 미납인 금액
	 *  2. 정상 납부해야하는 금액 및 마감 날짜
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetInterestResponseDto
	 * @see InterestRepository
	 */
	@Override
	public GetInterestResponseDto getInterest(Long memberId, Long groupId) {
		LocalDate endDate = LocalDate.now();

		Long overdueAmount = interestRepository.findOverAmountsByGroupIdAndActivatedFalseAndExpiredAtLessThanAndBilledAtLessThanEqual(
			groupId, endDate, endDate);
		Interest interest = interestRepository.findFirstByGroupIdAndActivatedFalseAndExpiredAtGreaterThanEqualAndBilledAtLessThanEqual(
			groupId, endDate, endDate).orElseGet(Interest::new);

		return GetInterestResponseDto.of(interest, overdueAmount);
	}
}
