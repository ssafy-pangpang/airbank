package com.pangpang.airbank.domain.loan.service;

import com.pangpang.airbank.domain.loan.dto.GetLoanResponseDto;

public interface LoanService {
	GetLoanResponseDto getLoan(Long memberId, Long groupId);
}
