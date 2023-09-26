package com.pangpang.airbank.domain.loan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.fund.domain.FundManagement;
import com.pangpang.airbank.domain.fund.repository.FundManagementRepository;
import com.pangpang.airbank.domain.loan.dto.GetLoanResponseDto;
import com.pangpang.airbank.global.error.exception.FundException;
import com.pangpang.airbank.global.error.info.FundErrorInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService {
	private final FundManagementRepository fundManagementRepository;

	/**
	 *  땡겨쓰기(한도, 땡겨쓴 금액)를 조회하는 메소드, 부모와 자녀가 조회 가능하다.
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetLoanResponseDto
	 * @see FundManagementRepository
	 */
	@Transactional(readOnly = true)
	@Override
	public GetLoanResponseDto getLoan(Long memberId, Long groupId) {
		FundManagement fundManagement = fundManagementRepository.findByGroupId(groupId)
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP_ID));
		return GetLoanResponseDto.from(fundManagement);
	}
}
