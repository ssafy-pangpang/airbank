package com.pangpang.airbank.domain.fund.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import com.pangpang.airbank.domain.member.service.MemberService;
import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.domain.notification.service.NotificationService;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.exception.FundException;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;
import com.pangpang.airbank.global.error.info.FundErrorInfo;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.meta.domain.AccountType;
import com.pangpang.airbank.global.meta.domain.CreditRating;
import com.pangpang.airbank.global.meta.domain.MemberRole;
import com.pangpang.airbank.global.meta.domain.NotificationType;
import com.pangpang.airbank.global.meta.domain.TaxRefund;
import com.pangpang.airbank.global.meta.domain.TransactionDistinction;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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
	private final MemberService memberService;
	private final NotificationService notificationService;
	private final ConfiscationConstantProvider confiscationConstantProvider;

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
	 * 모든 아이들에 대한 세금 환급 기능
	 */
	@Override
	@Transactional
	public void refundTaxes() {
		// 지난 달
		LocalDate preMonthDate = LocalDate.now().minusMonths(1);

		int limitCreditScore = CreditRating.THREE.getMinScore();

		List<Member> memberList = memberRepository.findAllByCreditScoreGreaterThanEqualAndRole(limitCreditScore,
			MemberRole.CHILD);

		for (Member member : memberList) {
			saveTaxRefund(member, preMonthDate);
		}
	}

	/**
	 * 지난 달 미납 세금 확인 기능, Cron
	 */
	@Override
	@Transactional
	public void checkNoPaymentTaxes() {
		// 지난 달 미납한 세금
		LocalDate preMonthDate = LocalDate.now().minusMonths(1);
		List<Tax> taxList = taxRepository.findAllByActivatedFalseAndExpiredAt_MonthValueAndExpiredAt_Year(preMonthDate);

		for (Tax tax : taxList) {
			createWarningTax(tax);
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
	 * 한 아이에 대한 세금 환급 계산
	 * @param member
	 * @param preMonthDate
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = RuntimeException.class)
	public void saveTaxRefund(Member member, LocalDate preMonthDate) {
		TaxRefund taxRefund = TaxRefund.ofCreditRating(
			CreditRating.getCreditRating(member.getCreditScore()).getRating());
		Group group = groupRepository.findByChildIdAndActivatedTrue(member.getId())
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_CHILD));

		// 저번 달 세금 냈는지 확인
		Tax tax = taxRepository.findByGroupIdAndExpiredAt_MonthValueAndExpiredAt_Year(group.getId(), preMonthDate)
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_TAX_BY_GROUP));
		if (!tax.getActivated()) {
			return;
		}

		// 환급할 세금
		Long refundAmount = Math.round(tax.getAmount() * taxRefund.getRefundRatio());
		if (refundAmount <= 0) {
			return;
		}

		// 이번 달에 아이가 세금 환급을 받았는지 체크
		Boolean existRefund = accountHistoryRepository.existsAccountHistoryByApiCreatedAtAndGroupIdAndTransactionType(
			member.getId(), LocalDate.now(), TransactionType.TAX_REFUND);
		if (existRefund) {
			return;
		}

		// 송금
		Account senderAccount = accountRepository.findByMemberIdAndType(group.getParent().getId(),
			AccountType.MAIN_ACCOUNT).orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));
		Account receiverAccount = accountRepository.findByMemberIdAndType(member.getId(),
			AccountType.MAIN_ACCOUNT).orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));
		transferService.transfer(TransferRequestDto.of(senderAccount, receiverAccount,
			refundAmount, TransactionType.TAX_REFUND));
	}

	/**
	 * 미납 세금 주인의 신용점수 하락 및 알림 발송
	 * @param tax
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = RuntimeException.class)
	public void createWarningTax(Tax tax) {
		// 신용 점수 하락
		memberService.updateCreditScoreByRate(tax.getGroup().getChild().getId(), -0.5);

		// 세금 미납 알림
		notificationService.saveNotification(
			CreateNotificationDto.of("지난 달 세금 미납으로 신용점수가 하락했습니다."
				, tax.getGroup().getChild(), NotificationType.TAX));
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

		// 신용 점수 증가
		try {
			memberService.updateCreditScoreByRate(memberId, 0.1);
			log.info(memberId + "신용 점수 수정 SUCCESS");
		} catch (RuntimeException e) {
			log.info(memberId + "신용 점수 수정 FAIL");
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

	/**
	 *  부모 계좌에서 자녀 계좌로 용돈을 자동이체 하는 메소드, Cron
	 *
	 * @see FundManagementRepository
	 * @see AccountRepository
	 * @see TransferService
	 */
	@Transactional
	@Override
	public void transferAllowanceByCron() {
		// TODO: 매일 오전 00시에 CRON 동작
		List<FundManagement> fundManagements = fundManagementRepository.findAll();
		Integer nowDay = LocalDate.now().getDayOfMonth();
		for (FundManagement fundManagement : fundManagements) {
			if (!fundManagement.getAllowanceDate().equals(nowDay)) {
				continue;
			}

			Group group = fundManagement.getGroup();
			Member parent = group.getParent();
			Member child = group.getChild();

			try {
				Account parentAccount = accountRepository.findByMemberAndType(parent, AccountType.MAIN_ACCOUNT)
					.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));

				Account childAccount = accountRepository.findByMemberAndType(child, AccountType.MAIN_ACCOUNT)
					.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));

				Optional<Confiscation> optionalConfiscation = confiscationRepository.findByGroupAndActivatedTrue(
					group);

				Long allowanceAmount = fundManagement.getAllowanceAmount();
				if (optionalConfiscation.isPresent()) {
					// 차압 상태면
					Confiscation confiscation = optionalConfiscation.get();

					Long remainAmount = confiscation.getAmount() - confiscation.getRepaidAmount();
					Long paymentAmount = (long)(allowanceAmount * (fundManagement.getConfiscationRate() / 100.0));

					if (remainAmount < paymentAmount) {
						allowanceAmount -= remainAmount;
						confiscation.plusReapidAmount(remainAmount);
					} else {
						allowanceAmount -= paymentAmount;
						confiscation.plusReapidAmount(paymentAmount);
					}

					// 압류금 전체를 변제 했다면 압류 비활성화 + 땡겨쓰기 초기화 + 전체 이자 납부 처리
					if (confiscation.getAmount().equals(confiscation.getRepaidAmount())) {
						confiscation.updateActivated(false);

						fundManagement.resetLoanLimitAndLoanAmount(
							fundManagement.getLoanLimit() - fundManagement.getLoanAmount(), 0L);

						// 이자 납부 처리
						List<Interest> interests = interestRepository.findAllByGroupAndActivatedFalseAndBilledAtLessThanEqual(
							group, LocalDate.now());
						for (Interest interest : interests) {
							interest.updateActivated(true);
						}
					}
				}

				TransferResponseDto response = transferService.transfer(
					TransferRequestDto.of(parentAccount, childAccount, allowanceAmount,
						TransactionType.ALLOWANCE));
				log.info(group.getId() + " 그룹 용돈 자동이체 SUCCESS / 이체 금액 : " + response.getAmount());
			} catch (RuntimeException e) {
				log.info(group.getId() + " 그룹 용돈 자동이체 FAIL");
			}
		}
	}

	/**
	 *  신용등급이 7등급 이하인 경우 압류하는 메소드, Cron
	 *
	 * @see FundManagementRepository
	 * @see ConfiscationRepository
	 */
	@Transactional
	@Override
	public void confiscateLoanByCron() {
		// TODO: 매일 오전 00시에 CRON 동작
		// 용돈 자동이체 메서드보다 먼저 실행되어야 함
		List<FundManagement> fundManagements = fundManagementRepository.findAll();
		Integer nowDay = LocalDate.now().getDayOfMonth();
		for (FundManagement fundManagement : fundManagements) {
			if (!fundManagement.getAllowanceDate().equals(nowDay)) {
				continue;
			}

			Group group = fundManagement.getGroup();
			Member child = group.getChild();

			// 자녀가 7등급 이하인지 확인
			if (CreditRating.getCreditRating(child.getCreditScore()).getRating()
				< confiscationConstantProvider.getConfiscationThreshold()) {
				continue;
			}

			// 현재 압류 중인지 확인
			if (confiscationRepository.existsByGroupIdAndActivatedTrue(group.getId())) {
				continue;
			}

			try {
				confiscateLoan(child.getId(), group.getId());
				notificationService.saveNotification(
					CreateNotificationDto.of("땡겨쓰기 잔액이 압류되었습니다.", child, NotificationType.CONFISCATION));
				log.info(group.getId() + "자동 압류 SUCCESS");
			} catch (RuntimeException e) {
				log.info(group.getId() + "자동 압류 FAIL");
			}
		}
	}

}
