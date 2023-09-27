package com.pangpang.airbank.domain.loan.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.dto.TransferRequestDto;
import com.pangpang.airbank.domain.account.dto.TransferResponseDto;
import com.pangpang.airbank.domain.account.repository.AccountRepository;
import com.pangpang.airbank.domain.account.service.TransferService;
import com.pangpang.airbank.domain.fund.domain.FundManagement;
import com.pangpang.airbank.domain.fund.repository.FundManagementRepository;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.group.repository.GroupRepository;
import com.pangpang.airbank.domain.loan.dto.GetLoanResponseDto;
import com.pangpang.airbank.domain.loan.dto.PostWithdrawLoanRequestDto;
import com.pangpang.airbank.domain.loan.dto.PostWithdrawLoanResponseDto;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.exception.FundException;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.LoanException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;
import com.pangpang.airbank.global.error.info.FundErrorInfo;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.LoanErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.meta.domain.AccountType;
import com.pangpang.airbank.global.meta.domain.CreditRating;
import com.pangpang.airbank.global.meta.domain.MemberRole;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService {
	private final FundManagementRepository fundManagementRepository;
	private final MemberRepository memberRepository;
	private final LoanConstantProvider loanConstantProvider;
	private final TransferService transferService;
	private final AccountRepository accountRepository;
	private final GroupRepository groupRepository;

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
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP));
		return GetLoanResponseDto.from(fundManagement);
	}

	/**
	 *  땡겨쓰기 가상계좌에서 자녀 계좌로 입금하는 메소드
	 *
	 * @param memberId Long
	 * @param postWithdrawLoanRequestDto PostWithdrawLoanRequestDto
	 * @return PostWithdrawLoanResponseDto
	 * @see MemberRepository
	 * @see LoanConstantProvider
	 * @see AccountRepository
	 * @see GroupRepository
	 * @see FundManagementRepository
	 * @see TransferService
	 */
	@Transactional
	@Override
	public PostWithdrawLoanResponseDto withdrawLoan(Long memberId,
		PostWithdrawLoanRequestDto postWithdrawLoanRequestDto) {
		Member child = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		Integer rating = CreditRating.getCreditRating(child.getCreditScore()).getRating();
		if (rating <= loanConstantProvider.getLoanThreshold()) {
			throw new LoanException(LoanErrorInfo.CREDIT_SCORE_BELOW_THRESHOLD);
		}

		if (!child.getRole().getName().equals(MemberRole.CHILD.getName())) {
			throw new LoanException(LoanErrorInfo.WITHDRAW_LOAN_PERMISSION_DENIED);
		}

		Account loanAccount = accountRepository.findByMemberIdAndType(memberId, AccountType.LOAN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_LOAN_ACCOUNT));

		Account mainAccount = accountRepository.findByMemberIdAndType(memberId, AccountType.MAIN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));

		Group group = groupRepository.findByChild(child)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_CHILD));

		FundManagement fundManagement = fundManagementRepository.findByGroup(group)
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP));

		if (fundManagement.getLoanAmount() + postWithdrawLoanRequestDto.getAmount() > fundManagement.getLoanLimit()) {
			throw new LoanException(LoanErrorInfo.LOAN_BALANCE_LIMIT_EXCEEDED);
		}

		// 땡겨쓰기 출금
		TransferRequestDto transferRequestDto = TransferRequestDto.of(loanAccount, mainAccount,
			postWithdrawLoanRequestDto.getAmount(), TransactionType.LOAN);
		TransferResponseDto response = transferService.transfer(transferRequestDto);
		fundManagement.plusLoanAmount(response.getAmount());

		return PostWithdrawLoanResponseDto.from(response.getAmount());
	}
}
