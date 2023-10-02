package com.pangpang.airbank.domain.fund.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.dto.SumAmountByAccount;
import com.pangpang.airbank.domain.account.dto.TransferRequestDto;
import com.pangpang.airbank.domain.account.dto.TransferResponseDto;
import com.pangpang.airbank.domain.account.repository.AccountHistoryRepository;
import com.pangpang.airbank.domain.account.repository.AccountRepository;
import com.pangpang.airbank.domain.account.service.TransferService;
import com.pangpang.airbank.domain.fund.domain.Confiscation;
import com.pangpang.airbank.domain.fund.domain.FundManagement;
import com.pangpang.airbank.domain.fund.domain.Interest;
import com.pangpang.airbank.domain.fund.domain.Tax;
import com.pangpang.airbank.domain.fund.dto.GetConfiscationResponseDto;
import com.pangpang.airbank.domain.fund.dto.GetInterestResponseDto;
import com.pangpang.airbank.domain.fund.dto.GetTaxResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferBonusResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferConfiscationRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferConfiscationResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferInterestRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferInterestResponseDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferTaxRequestDto;
import com.pangpang.airbank.domain.fund.dto.PostTransferTaxResponseDto;
import com.pangpang.airbank.domain.fund.repository.ConfiscationRepository;
import com.pangpang.airbank.domain.fund.repository.FundManagementRepository;
import com.pangpang.airbank.domain.fund.repository.InterestRepository;
import com.pangpang.airbank.domain.fund.repository.TaxRepository;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.group.repository.GroupRepository;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.exception.FundException;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;
import com.pangpang.airbank.global.error.info.FundErrorInfo;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.meta.domain.AccountType;
import com.pangpang.airbank.global.meta.domain.MemberRole;
import com.pangpang.airbank.global.meta.domain.TransactionDistinction;
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
	private final ConfiscationRepository confiscationRepository;
	private final MemberRepository memberRepository;
	private final FundManagementRepository fundManagementRepository;
	private final AccountHistoryRepository accountHistoryRepository;

	/**
	 *  현재 세금 현황 조회
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetTaxResponseDto
	 * @see TaxRepository
	 */
	@Transactional(readOnly = true)
	@Override
	public GetTaxResponseDto getTax(Long memberId, Long groupId) {
		LocalDate endDate = LocalDate.now();
		// 밀린 세금의 기준 날짜
		endDate = endDate.withDayOfMonth(1);

		Long overdueAmount = overdueTaxAmount(groupId, endDate);
		Tax tax = curMonthTax(groupId, endDate);

		return GetTaxResponseDto.of(tax, overdueAmount);
	}

	/**
	 * 세금 송금
	 *
	 * @param memberId
	 * @param postTransferTaxRequestDto
	 * @return PostTransferTaxResponseDto
	 * @see GroupRepository
	 * @see AccountRepository
	 * @see TransferService
	 */
	@Transactional
	@Override
	public PostTransferTaxResponseDto transferTax(Long memberId, PostTransferTaxRequestDto postTransferTaxRequestDto) {
		if (postTransferTaxRequestDto.getAmount().equals(0L)) {
			throw new FundException(FundErrorInfo.NOT_FOUND_TRANSFER_AMOUNT);
		}
		Group group = groupRepository.findByChildIdAndActivatedTrue(memberId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_ID));

		// 세금 총 합과 송금 금액이 같은지 확인
		List<Tax> taxList = taxRepository.findAllByGroupAndActivatedFalse(group);
		Long sumTax = 0L;
		for (Tax tax : taxList) {
			sumTax += tax.getAmount();
		}

		if (!postTransferTaxRequestDto.getAmount().equals(sumTax)) {
			throw new FundException(FundErrorInfo.NOT_MATCH_AMOUNT);
		}

		// 송금
		Account senderAccount = accountRepository.findByMemberIdAndType(memberId, AccountType.MAIN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));
		Account receiverAccount = accountRepository.findByMemberIdAndType(group.getParent().getId(),
			AccountType.MAIN_ACCOUNT).orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));
		TransferResponseDto transferResponseDto = transferService.transfer(
			TransferRequestDto.of(senderAccount, receiverAccount, postTransferTaxRequestDto.getAmount(),
				TransactionType.TAX));

		// 세금 납부 처리
		for (Tax tax : taxList) {
			tax.updateActivated(true);
		}

		return PostTransferTaxResponseDto.of(transferResponseDto, TransactionType.TAX);
	}

	/**
	 * 세금 부과 기능 (cron에서 사용)
	 * @see AccountHistoryRepository
	 */
	@Override
	@Transactional
	public void createTaxes() {
		LocalDate standDate = LocalDate.now();
		standDate = standDate.withDayOfMonth(1);
		standDate = standDate.minusDays(1);
		LocalDate expiredDate = curLastDate();

		List<SumAmountByAccount> taxList = accountHistoryRepository.findAmountSumByAccountAndApiCreatedAt(standDate,
			new TransactionType[] {TransactionType.BONUS, TransactionType.MISSION},
			new TransactionDistinction[] {TransactionDistinction.DEPOSIT});

		for (SumAmountByAccount tax : taxList) {
			saveTax(tax, expiredDate);
		}
	}

	/**
	 * 밀린 세금 금액
	 *
	 * @param groupId
	 * @param date
	 * @return Long
	 */
	public Long overdueTaxAmount(Long groupId, LocalDate date) {
		return taxRepository.findOverAmountsByGroupIdAndActivatedFalseAndExpiredAtLessThan(groupId, date);
	}

	/**
	 * 이번 달 세금
	 *
	 * @param groupId
	 * @param date
	 * @return Tax
	 */
	public Tax curMonthTax(Long groupId, LocalDate date) {
		return taxRepository.findByGroupIdAndActivatedFalseAndExpiredAtGreaterThanEqual(groupId, date)
			.orElseGet(Tax::new);
	}

	/**
	 * 한 사용자의 세금 저장
	 *
	 * @param tax
	 * @see TaxRepository
	 * @see FundManagementRepository
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = RuntimeException.class)
	public void saveTax(SumAmountByAccount tax, LocalDate expiredDate) {
		Group group = groupRepository.findByChildIdAndActivatedTrue(tax.getMemberId())
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_CHILD));

		// 이미 세금이 부과된 경우
		if (curMonthTax(group.getId(), expiredDate).getAmount() != null) {
			return;
		}

		FundManagement fundManagement = fundManagementRepository.findByGroup(group)
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP));
		Long taxAmount = Math.round(tax.getSumAmount() * (fundManagement.getTaxRate() / 100.0));

		// 세금이 0인 경우
		if (taxAmount <= 0) {
			return;
		}

		taxRepository.save(Tax.of(taxAmount, expiredDate, group));
	}

	/**
	 * 현재 달의 마지막 날짜
	 * @return LocalDate
	 */
	public LocalDate curLastDate() {
		LocalDate expiredDate = LocalDate.now();
		expiredDate = expiredDate.plusMonths(1);
		expiredDate = expiredDate.withDayOfMonth(1);
		expiredDate = expiredDate.minusDays(1);
		return expiredDate;
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
	@Transactional(readOnly = true)
	@Override
	public GetInterestResponseDto getInterest(Long memberId, Long groupId) {
		LocalDate endDate = LocalDate.now();

		Long overdueAmount =
			interestRepository.findOverAmountsByGroupIdAndActivatedFalseAndExpiredAtLessThanAndBilledAtLessThanEqual(
				groupId, endDate, endDate);
		Interest interest =
			interestRepository.findFirstByGroupIdAndActivatedFalseAndExpiredAtGreaterThanEqualAndBilledAtLessThanEqual(
				groupId, endDate, endDate).orElseGet(Interest::new);

		return GetInterestResponseDto.of(interest, overdueAmount);
	}

	/**
	 *  이자 전체 상환
	 *
	 * @param memberId Long
	 * @param postTransferInterestRequestDto PostTransferInterestRequestDto
	 * @return PostTransferInterestResponseDto
	 * @see GroupRepository
	 * @see InterestRepository
	 * @see TransferService
	 */
	@Transactional
	@Override
	public PostTransferInterestResponseDto transferInterest(Long memberId,
		PostTransferInterestRequestDto postTransferInterestRequestDto) {

		if (postTransferInterestRequestDto.getAmount().equals(0L)) {
			throw new FundException(FundErrorInfo.NOT_FOUND_TRANSFER_AMOUNT);
		}
		Group group = groupRepository.findByChildIdAndActivatedTrue(memberId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_ID));

		// 이자 총 합과 송금 금액이 같은지 확인
		List<Interest> interestList = interestRepository.findAllByGroupAndActivatedFalseAndBilledAtLessThanEqual(group,
			LocalDate.now());
		Long sumInterest = 0L;
		for (Interest interest : interestList) {
			sumInterest += interest.getAmount();
		}

		if (!postTransferInterestRequestDto.getAmount().equals(sumInterest)) {
			throw new FundException(FundErrorInfo.NOT_MATCH_AMOUNT);
		}

		// 송금
		Account senderAccount = accountRepository.findByMemberIdAndType(memberId, AccountType.MAIN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));
		Account receiverAccount = accountRepository.findByMemberIdAndType(group.getParent().getId(),
			AccountType.MAIN_ACCOUNT).orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));
		TransferResponseDto transferResponseDto = transferService.transfer(
			TransferRequestDto.of(senderAccount, receiverAccount, postTransferInterestRequestDto.getAmount(),
				TransactionType.INTEREST));

		// 이자 납부 처리
		for (Interest interest : interestList) {
			interest.updateActivated(true);
		}

		return PostTransferInterestResponseDto.of(transferResponseDto, TransactionType.INTEREST);
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

	/**
	 *  압류 조회
	 *
	 * @param groupId Long
	 * @return GetConfiscationResponseDto
	 * @see ConfiscationRepository
	 */
	@Transactional(readOnly = true)
	@Override
	public GetConfiscationResponseDto getConfiscation(Long groupId) {
		Confiscation confiscation = confiscationRepository.findByGroupIdAndActivatedTrue(groupId)
			.orElseGet(Confiscation::new);

		return GetConfiscationResponseDto.from(confiscation);
	}

	/**
	 * 변제금 송금
	 *
	 * @param memberId
	 * @param postTransferConfiscationRequestDto
	 * @return PostTransferConfiscationResponseDto
	 * @see MemberRepository
	 * @see GroupRepository
	 * @see ConfiscationRepository
	 * @see AccountRepository
	 * @see TransferService
	 * @see FundManagementRepository
	 */
	@Transactional
	@Override
	public PostTransferConfiscationResponseDto transferConfiscation(Long memberId,
		PostTransferConfiscationRequestDto postTransferConfiscationRequestDto) {
		Member child = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		if (!child.getRole().getName().equals(MemberRole.CHILD.getName())) {
			throw new FundException(FundErrorInfo.REPAY_CONFISCATION_PERMISSION_DENIED);
		}

		if (postTransferConfiscationRequestDto.getAmount().equals(0L)) {
			throw new FundException(FundErrorInfo.NOT_FOUND_TRANSFER_AMOUNT);
		}

		Group group = groupRepository.findByChildIdAndActivatedTrue(memberId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_ID));

		Confiscation confiscation = confiscationRepository.findByGroupAndActivatedTrue(group)
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_CONFISCATION_BY_GROUP));

		// 남은 압류금 이하의 금액인지 확인
		Long remainAmount = confiscation.getAmount() - confiscation.getRepaidAmount();

		if (remainAmount < postTransferConfiscationRequestDto.getAmount()) {
			throw new FundException(FundErrorInfo.CONFISCATION_AMOUNT_EXCEEDED);
		}

		// 송금
		Account senderAccount = accountRepository.findByMemberIdAndType(memberId, AccountType.MAIN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));

		Account receiverAccount = accountRepository.findByMemberIdAndType(group.getParent().getId(),
			AccountType.MAIN_ACCOUNT).orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));

		TransferResponseDto transferResponseDto = transferService.transfer(
			TransferRequestDto.of(senderAccount, receiverAccount, postTransferConfiscationRequestDto.getAmount(),
				TransactionType.CONFISCATION));

		// 변제금 업데이트
		confiscation.plusReapidAmount(postTransferConfiscationRequestDto.getAmount());

		// 압류금 전체를 변제 했다면 압류 비활성화 + 땡겨쓰기 초기화 + 전체 이자 납부 처리
		if (confiscation.getAmount().equals(confiscation.getRepaidAmount())) {
			confiscation.updateActivated(false);

			// 땡겨쓰기 제한 금액 및 땡겨쓴 금액 재설정
			FundManagement fundManagement = fundManagementRepository.findByGroup(group)
				.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP));

			Long newLoanLimit = fundManagement.getLoanLimit() - fundManagement.getLoanAmount();

			fundManagement.resetLoanLimitAndLoanAmount(newLoanLimit, 0L);

			// 이자 납부 처리
			List<Interest> interestList = interestRepository.findAllByGroupAndActivatedFalseAndBilledAtLessThanEqual(
				group, LocalDate.now());
			for (Interest interest : interestList) {
				interest.updateActivated(true);
			}
		}

		return PostTransferConfiscationResponseDto.of(transferResponseDto, TransactionType.CONFISCATION);
	}

	/**
	 *  압류 시작
	 *
	 * @param childId Long
	 * @param groupId Long
	 * @return TransferResponseDto
	 * @see FundManagementRepository
	 * @see GroupRepository
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void confiscateLoan(Long childId, Long groupId) {
		FundManagement fundManagement = fundManagementRepository.findByGroupId(groupId)
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP));

		GetInterestResponseDto getInterestResponseDto = getInterest(childId, groupId);

		Group group = groupRepository.findById(groupId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_ID));

		Long confiscationAmount = fundManagement.getLoanAmount() + getInterestResponseDto.getAmount()
			+ getInterestResponseDto.getOverdueAmount();

		confiscationRepository.save(Confiscation.of(confiscationAmount, group));
	}
}
