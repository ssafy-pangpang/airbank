package com.pangpang.airbank.domain.loan.service;

import com.pangpang.airbank.domain.loan.dto.GetLoanResponseDto;
import com.pangpang.airbank.domain.loan.dto.PostCommonLoanRequestDto;
import com.pangpang.airbank.domain.loan.dto.PostRepaidLoanResponseDto;
import com.pangpang.airbank.domain.loan.dto.PostWithdrawLoanResponseDto;

public interface LoanService {
	GetLoanResponseDto getLoan(Long memberId, Long groupId);

	PostWithdrawLoanResponseDto withdrawLoan(Long memberId, PostCommonLoanRequestDto postCommonLoanRequestDto);

	PostRepaidLoanResponseDto repaidLoan(Long memberId, PostCommonLoanRequestDto postCommonLoanRequestDto);
}
