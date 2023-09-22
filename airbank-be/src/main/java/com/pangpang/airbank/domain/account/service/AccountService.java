package com.pangpang.airbank.domain.account.service;

import com.pangpang.airbank.domain.account.dto.CommonAccountIdResponseDto;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;

public interface AccountService {
	CommonAccountIdResponseDto saveAccount(PostEnrollAccountRequestDto postEnrollAccountRequestDto,
		Long memberId);
}
