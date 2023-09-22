package com.pangpang.airbank.domain.account.service;

import com.pangpang.airbank.domain.account.dto.CommonAccountIdResponseDto;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.global.meta.domain.AccountType;

public interface AccountService {
	CommonAccountIdResponseDto saveAccount(PostEnrollAccountRequestDto postEnrollAccountRequestDto,
		Long memberId);

	String getAccountNumber(Long memberId, AccountType type);

	String getFinAccountNumber(Long memberId, AccountType type);
}
