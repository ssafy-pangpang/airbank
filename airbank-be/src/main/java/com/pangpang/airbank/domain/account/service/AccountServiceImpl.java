package com.pangpang.airbank.domain.account.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.dto.GetAccountAmountResponseDto;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.domain.account.repository.AccountRepository;
import com.pangpang.airbank.domain.fund.repository.FundManagementRepository;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.common.api.nh.NHApi;
import com.pangpang.airbank.global.common.api.nh.dto.GetCheckFinAccountResponseDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetFinAccountResponseDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetInquireBalanceResponseDto;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;
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
	private final FundManagementRepository fundManagementRepository;
	private final NHApi nhApi;

	/**
	 *  사용자가 계좌 등록하는 과정 처리
	 *
	 * @param postEnrollAccountRequestDto PostEnrollAccountRequestDto
	 * @return CommonIdResponseDto
	 * @see MemberRepository
	 * @see NHApi
	 */
	@Override
	@Transactional
	public CommonIdResponseDto saveMainAccount(PostEnrollAccountRequestDto postEnrollAccountRequestDto, Long memberId) {
		Member member = memberRepository.getReferenceById(memberId);

		// 계좌 저장
		Account account = Account.of(postEnrollAccountRequestDto, member, AccountType.MAIN_ACCOUNT);
		accountRepository.save(account);

		// 핀-어카운트 저장
		saveFinAccount(account, postEnrollAccountRequestDto);
		return CommonIdResponseDto.from(account.getId());
	}

	/**
	 *  사용자가에게 사용 가능한 땡겨쓰기, 티끌모으기 가상 계좌를 발급해주는 메소드
	 *
	 * @param memberId Long
	 * @return CommonIdResponseDto
	 * @see AccountRepository
	 * @see MemberRepository
	 */
	@Override
	@Transactional
	public void saveVirtualAccount(Long memberId, AccountType type) {
		Account account = accountRepository.findFirstByMemberIsNullAndType(type)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_AVAILABLE_ACCOUNT));

		PostEnrollAccountRequestDto postEnrollAccountRequestDto = PostEnrollAccountRequestDto.fromVirtualAccount(
			account.getAccountNumber());

		saveFinAccount(account, postEnrollAccountRequestDto);
		account.addMember(memberRepository.getReferenceById(memberId));
	}

	/**
	 *  들어오는 계좌번호에 대해 핀-어카운트를 발급하고 저장하는 메소드
	 *
	 * @param account Account
	 * @param postEnrollAccountRequestDto PostEnrollAccountRequestDto
	 * @return void
	 * @see NHApi
	 */
	private void saveFinAccount(Account account, PostEnrollAccountRequestDto postEnrollAccountRequestDto) {
		// 핀-어카운트 직접 발급
		GetFinAccountResponseDto getFinAccountResponseDto;
		try {
			getFinAccountResponseDto = nhApi.getFinAccountDirect(postEnrollAccountRequestDto);
		} catch (Exception e) {
			throw new AccountException(AccountErrorInfo.ACCOUNT_NH_SERVER_ERROR);
		}

		// 핀-어카운트 발급 확인
		GetCheckFinAccountResponseDto getCheckFinAccountResponseDto;
		try {
			getCheckFinAccountResponseDto = nhApi.checkOpenFinAccountDirect(
				getFinAccountResponseDto.getRgno());
		} catch (Exception e) {
			throw new AccountException(AccountErrorInfo.ACCOUNT_NH_SERVER_ERROR);
		}
		account.addFinAccount(getCheckFinAccountResponseDto.getFinAcno());
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

	/**
	 *  메인 계좌의 잔액 조회
	 *
	 * @param memberId Long
	 * @return GetAccountAmountResponseDto
	 * @see NHApi
	 * @see GetInquireBalanceResponseDto
	 */
	@Override
	public GetAccountAmountResponseDto getAccountAmount(Long memberId) {
		AccountType accountType = AccountType.ofName("MAIN_ACCOUNT");
		String finAccount = getFinAccountNumber(memberId, accountType);

		GetInquireBalanceResponseDto response;
		try {
			response = nhApi.getAccountAmount(finAccount);
		} catch (Exception e) {
			throw new AccountException(AccountErrorInfo.ACCOUNT_NH_SERVER_ERROR);
		}
		return GetAccountAmountResponseDto.from(response);
	}

	private Account getAccount(Long memberId, AccountType type) {
		return accountRepository.findByMemberIdAndType(memberId, type)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));
	}

}
