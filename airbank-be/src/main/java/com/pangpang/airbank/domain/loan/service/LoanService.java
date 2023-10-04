package com.pangpang.airbank.domain.loan.service;

import com.pangpang.airbank.domain.loan.dto.GetLoanResponseDto;
import com.pangpang.airbank.domain.loan.dto.PostCommonLoanRequestDto;
import com.pangpang.airbank.domain.loan.dto.PostRepaidLoanResponseDto;
import com.pangpang.airbank.global.common.response.CommonAmountResponseDto;

public interface LoanService {
	GetLoanResponseDto getLoan(Long memberId, Long groupId);

	PostRepaidLoanResponseDto repaidLoan(Long memberId, PostCommonLoanRequestDto postCommonLoanRequestDto);

	CommonAmountResponseDto withdrawLoan(Long memberId, PostCommonLoanRequestDto postCommonLoanRequestDto);

	void createInterestByCron();
}
