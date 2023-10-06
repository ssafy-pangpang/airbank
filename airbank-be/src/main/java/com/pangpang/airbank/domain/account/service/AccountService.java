package com.pangpang.airbank.domain.account.service;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.dto.GetAccountAmountResponseDto;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;
import com.pangpang.airbank.global.meta.domain.AccountType;

public interface AccountService {
	CommonIdResponseDto saveMainAccount(PostEnrollAccountRequestDto postEnrollAccountRequestDto, Long memberId);

	Account saveVirtualAccount(Long memberId, AccountType accountType);

	String getAccountNumber(Long memberId, AccountType type);

	String getFinAccountNumber(Long memberId, AccountType type);

	GetAccountAmountResponseDto getAccountAmount(Long memberId);
}
