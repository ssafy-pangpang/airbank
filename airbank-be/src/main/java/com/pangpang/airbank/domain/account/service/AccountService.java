package com.pangpang.airbank.domain.account.service;

import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;
import com.pangpang.airbank.global.meta.domain.AccountType;

public interface AccountService {
	CommonIdResponseDto saveAccount(PostEnrollAccountRequestDto postEnrollAccountRequestDto, Long memberId);

	String getAccountNumber(Long memberId, AccountType type);

	String getFinAccountNumber(Long memberId, AccountType type);
}
