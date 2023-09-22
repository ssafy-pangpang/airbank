package com.pangpang.airbank.domain.account.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.dto.CommonAccountIdResponseDto;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.domain.account.repository.AccountRepository;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.common.api.nh.NHApi;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;
import com.pangpang.airbank.global.meta.domain.AccountType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
	private final MemberRepository memberRepository;
	private final AccountRepository accountRepository;
	private final NHApi nhApi;

	@Override
	@Transactional
	public CommonAccountIdResponseDto saveAccount(PostEnrollAccountRequestDto postEnrollAccountRequestDto,
		Long memberId) {
		// Member member = memberRepository.findById(memberId)
		// 	.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		try {
			System.out.println(nhApi.getFinAccountDirect(postEnrollAccountRequestDto));
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}

		return null;

	}

	@Transactional(readOnly = true)
	@Override
	public String getAccountNumber(Long memberId, AccountType type) {
		return getAccount(memberId, type).getAccountNumber();
	}

	@Transactional(readOnly = true)
	@Override
	public String getFinAccountNumber(Long memberId, AccountType type) {
		return getAccount(memberId, type).getFinAccountNumber();
	}

	private Account getAccount(Long memberId, AccountType type) {
		return accountRepository.findByMemberIdAndType(memberId, type)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));
	}

}
