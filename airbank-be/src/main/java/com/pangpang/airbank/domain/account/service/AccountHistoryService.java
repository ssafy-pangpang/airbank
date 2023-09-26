package com.pangpang.airbank.domain.account.service;

import java.util.UUID;

import com.pangpang.airbank.domain.account.dto.SaveDepositHistoryRequestDto;
import com.pangpang.airbank.domain.account.dto.SaveWithdrawalHistoryRequestDto;

public interface AccountHistoryService {
	UUID saveWithdrawalHistory(SaveWithdrawalHistoryRequestDto saveWithdrawalHistoryRequestDto);

	UUID saveDepositHistory(SaveDepositHistoryRequestDto saveDepositHistoryRequestDto);
}
