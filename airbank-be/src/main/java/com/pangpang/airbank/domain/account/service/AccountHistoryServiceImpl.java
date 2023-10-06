package com.pangpang.airbank.domain.account.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.domain.AccountHistory;
import com.pangpang.airbank.domain.account.dto.AccountHistoryElement;
import com.pangpang.airbank.domain.account.dto.GetAccountHistoryResponseDto;
import com.pangpang.airbank.domain.account.dto.SaveDepositHistoryRequestDto;
import com.pangpang.airbank.domain.account.dto.SaveWithdrawalHistoryRequestDto;
import com.pangpang.airbank.domain.account.repository.AccountHistoryRepository;
import com.pangpang.airbank.domain.account.repository.AccountRepository;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;
import com.pangpang.airbank.global.meta.domain.AccountType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountHistoryServiceImpl implements AccountHistoryService {
	private static final String MAIN_ACCOUNT = "main";
	private static final String LOAN_ACCOUNT = "loan";
	private static final String SAVINGS_ACCOUNT = "savings";

	private final AccountHistoryRepository accountHistoryRepository;
	private final AccountRepository accountRepository;

	@Override
	public UUID saveWithdrawalHistory(SaveWithdrawalHistoryRequestDto saveWithdrawalHistoryRequestDto) {
		AccountHistory accountHistory = AccountHistory.from(saveWithdrawalHistoryRequestDto);
		accountHistoryRepository.save(accountHistory);

		return accountHistory.getTransactionIdentifier();
	}

	@Override
	public UUID saveDepositHistory(SaveDepositHistoryRequestDto saveDepositHistoryRequestDto) {
		AccountHistory accountHistory = AccountHistory.from(saveDepositHistoryRequestDto);
		accountHistoryRepository.save(accountHistory);

		return accountHistory.getTransactionIdentifier();
	}

	/**
	 * 거래 내역 조회
	 *
	 * @param memberId
	 * @param accountType
	 * @return GetAccountHistoryResponseDto
	 */
	@Transactional(readOnly = true)
	@Override
	public GetAccountHistoryResponseDto inquireAccountHistory(Long memberId, String accountType) {
		AccountType type = getAccountType(accountType);
		Account account = accountRepository.findByMemberIdAndType(memberId, type)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.ACCOUNT_SERVER_ERROR));

		List<AccountHistoryElement> accountHistoryElements
			= accountHistoryRepository.findAllAccountHistoryByAccount(account);

		return GetAccountHistoryResponseDto.from(accountHistoryElements);
	}

	/**
	 * 계좌 타입 조회
	 *
	 * @param accountType
	 * @return AccountType
	 */
	private AccountType getAccountType(String accountType) {
		if (accountType.equals(MAIN_ACCOUNT)) {
			return AccountType.MAIN_ACCOUNT;
		} else if (accountType.equals(LOAN_ACCOUNT)) {
			return AccountType.LOAN_ACCOUNT;
		} else if (accountType.equals(SAVINGS_ACCOUNT)) {
			return AccountType.SAVINGS_ACCOUNT;
		}

		throw new AccountException(AccountErrorInfo.ACCOUNT_HISTORY_SERVER_ERROR);
	}
}
