package com.pangpang.airbank.domain.fund.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.fund.domain.Interest;
import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.dto.TransferRequestDto;
import com.pangpang.airbank.domain.account.dto.TransferResponseDto;
import com.pangpang.airbank.domain.account.repository.AccountRepository;
import com.pangpang.airbank.domain.account.service.TransferService;
import com.pangpang.airbank.domain.fund.domain.Tax;
import com.pangpang.airbank.domain.fund.dto.GetInterestResponseDto;
import com.pangpang.airbank.domain.fund.dto.GetTaxResponseDto;
import com.pangpang.airbank.domain.fund.repository.InterestRepository;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusResponseDto;
import com.pangpang.airbank.domain.fund.repository.TaxRepository;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.group.repository.GroupRepository;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.meta.domain.AccountType;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {
	private final TransferService transferService;
	private final TaxRepository taxRepository;
	private final InterestRepository interestRepository;
	private final GroupRepository groupRepository;
	private final AccountRepository accountRepository;

	/**
	 *  현재 세금 현황 조회
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetTaxResponseDto
	 * @see TaxRepository
	 */
	@Override
	public GetTaxResponseDto getTax(Long memberId, Long groupId) {
		LocalDate endDate = LocalDate.now();
		// 밀린 세금의 기준 날짜
		endDate = endDate.withDayOfMonth(1);

		Long overdueAmount = taxRepository.findOverAmountsByGroupIdAndActivatedFalseAndExpiredAtLessThan(groupId,
			endDate);
		Tax tax = taxRepository.findByGroupIdAndActivatedFalseAndExpiredAtGreaterThanEqual(groupId, endDate)
			.orElseGet(Tax::new);

		return GetTaxResponseDto.of(tax, overdueAmount);
	}

	/**
	 *  이자 조회
	 *  1. 오늘 기준으로 이자 미납인 금액
	 *  2. 정상 납부해야하는 금액 및 마감 날짜
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetInterestResponseDto
	 * @see InterestRepository
	 */
	@Override
	public GetInterestResponseDto getInterest(Long memberId, Long groupId) {
		LocalDate endDate = LocalDate.now();

		Long overdueAmount = interestRepository.findOverAmountsByGroupIdAndActivatedFalseAndExpiredAtLessThanAndBilledAtLessThanEqual(
			groupId, endDate, endDate);
		Interest interest = interestRepository.findFirstByGroupIdAndActivatedFalseAndExpiredAtGreaterThanEqualAndBilledAtLessThanEqual(
			groupId, endDate, endDate).orElseGet(Interest::new);

		return GetInterestResponseDto.of(interest, overdueAmount);
	}

	/**
	 * 보너스 송금
	 *
	 * @param postTransferBonusRequestDto
	 * @param memberId
	 * @param groupId
	 * @return PostTransferBonusResponseDto
	 */
	@Transactional
	@Override
	public PostTransferBonusResponseDto transferBonus(PostTransferBonusRequestDto postTransferBonusRequestDto,
		Long memberId, Long groupId) {
		Group group = groupRepository.findById(groupId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_ID));

		Long childId = group.getChild().getId();

		Account senderAccount = accountRepository.findByMemberIdAndType(memberId, AccountType.MAIN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));

		Account receiverAccount = accountRepository.findByMemberIdAndType(childId, AccountType.MAIN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));

		TransferResponseDto transferResponseDto = transferService.transfer(
			TransferRequestDto.of(senderAccount, receiverAccount, postTransferBonusRequestDto.getAmount(),
				TransactionType.BONUS));

		return PostTransferBonusResponseDto.from(transferResponseDto);
	}
}
