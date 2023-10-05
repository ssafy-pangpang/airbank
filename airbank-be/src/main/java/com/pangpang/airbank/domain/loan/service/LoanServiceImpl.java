package com.pangpang.airbank.domain.loan.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.dto.TransferRequestDto;
import com.pangpang.airbank.domain.account.dto.TransferResponseDto;
import com.pangpang.airbank.domain.account.repository.AccountRepository;
import com.pangpang.airbank.domain.account.service.TransferService;
import com.pangpang.airbank.domain.fund.domain.FundManagement;
import com.pangpang.airbank.domain.fund.domain.Interest;
import com.pangpang.airbank.domain.fund.dto.GetInterestResponseDto;
import com.pangpang.airbank.domain.fund.repository.ConfiscationRepository;
import com.pangpang.airbank.domain.fund.repository.FundManagementRepository;
import com.pangpang.airbank.domain.fund.repository.InterestRepository;
import com.pangpang.airbank.domain.fund.service.FundService;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.group.repository.GroupRepository;
import com.pangpang.airbank.domain.loan.dto.GetLoanResponseDto;
import com.pangpang.airbank.domain.loan.dto.PostCommonLoanRequestDto;
import com.pangpang.airbank.domain.loan.dto.PostRepaidLoanResponseDto;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.domain.member.service.MemberService;
import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.domain.notification.service.NotificationService;
import com.pangpang.airbank.global.common.response.CommonAmountResponseDto;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.exception.FundException;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.LoanException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;
import com.pangpang.airbank.global.error.info.FundErrorInfo;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.LoanErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.meta.domain.AccountType;
import com.pangpang.airbank.global.meta.domain.CreditRating;
import com.pangpang.airbank.global.meta.domain.InterestRate;
import com.pangpang.airbank.global.meta.domain.MemberRole;
import com.pangpang.airbank.global.meta.domain.NotificationType;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl implements LoanService {
	private final FundManagementRepository fundManagementRepository;
	private final MemberRepository memberRepository;
	private final LoanConstantProvider loanConstantProvider;
	private final TransferService transferService;
	private final AccountRepository accountRepository;
	private final GroupRepository groupRepository;
	private final FundService fundService;
	private final InterestRepository interestRepository;
	private final MemberService memberService;
	private final ConfiscationRepository confiscationRepository;
	private final NotificationService notificationService;

	/**
	 *  땡겨쓰기(한도, 땡겨쓴 금액)를 조회하는 메소드, 부모와 자녀가 조회 가능하다.
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetLoanResponseDto
	 * @see FundManagementRepository
	 */
	@Transactional(readOnly = true)
	@Override
	public GetLoanResponseDto getLoan(Long memberId, Long groupId) {
		FundManagement fundManagement = fundManagementRepository.findByGroupId(groupId)
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP));
		return GetLoanResponseDto.from(fundManagement);
	}

	/**
	 *  땡겨쓰기 가상계좌에서 자녀 계좌로 입금하는 메소드
	 *
	 * @param memberId Long
	 * @param postCommonLoanRequestDto PostCommonLoanRequestDto
	 * @return CommonAmountResponseDto
	 * @see MemberRepository
	 * @see LoanConstantProvider
	 * @see AccountRepository
	 * @see GroupRepository
	 * @see FundManagementRepository
	 * @see FundService
	 * @see InterestRepository
	 * @see TransferService
	 */
	@Transactional
	@Override
	public CommonAmountResponseDto withdrawLoan(Long memberId,
		PostCommonLoanRequestDto postCommonLoanRequestDto) {
		Member child = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		Integer rating = CreditRating.getCreditRating(child.getCreditScore()).getRating();
		if (rating <= loanConstantProvider.getLoanThreshold()) {
			throw new LoanException(LoanErrorInfo.CREDIT_SCORE_BELOW_THRESHOLD);
		}

		if (!child.getRole().getName().equals(MemberRole.CHILD.getName())) {
			throw new LoanException(LoanErrorInfo.WITHDRAW_LOAN_PERMISSION_DENIED);
		}

		Group group = groupRepository.findByChild(child)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_CHILD));

		if (confiscationRepository.existsByGroupAndActivatedTrue(group)) {
			throw new LoanException(LoanErrorInfo.CONFISCATION_IN_PROCEEDING);
		}

		Account loanAccount = accountRepository.findByMemberAndType(child, AccountType.LOAN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_LOAN_ACCOUNT));

		Account mainAccount = accountRepository.findByMemberAndType(child, AccountType.MAIN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));

		FundManagement fundManagement = fundManagementRepository.findByGroup(group)
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP));

		if (fundManagement.getLoanAmount() + postCommonLoanRequestDto.getAmount() > fundManagement.getLoanLimit()) {
			throw new LoanException(LoanErrorInfo.LOAN_BALANCE_LIMIT_EXCEEDED);
		}

		// 이자 첫 Row 생성
		Optional<Interest> oldInterest = interestRepository.findByGroupAndActivatedFalseAndBilledAtGreaterThan(
			group, LocalDate.now());

		if (oldInterest.isEmpty()) {
			Interest interest = Interest.of(group);
			interestRepository.save(interest);
		}

		// 땡겨쓰기 출금
		TransferRequestDto transferRequestDto = TransferRequestDto.of(loanAccount, mainAccount,
			postCommonLoanRequestDto.getAmount(), TransactionType.LOAN);
		TransferResponseDto response = transferService.transfer(transferRequestDto);
		fundManagement.plusLoanAmount(response.getAmount());

		// 신용 점수 감소
		try {
			memberService.updateCreditScoreByRate(memberId, -0.3);
			log.info(memberId + "신용 점수 수정 SUCCESS");
		} catch (RuntimeException e) {
			log.info(memberId + "신용 점수 수정 FAIL");
		}

		return CommonAmountResponseDto.from(response.getAmount());
	}

	/**
	 *  자녀 계좌에서 땡겨쓰기 가상 계좌로 입금하는 메소드
	 *
	 * @param memberId Long
	 * @param postCommonLoanRequestDto PostCommonLoanRequestDto
	 * @return PostRepaidLoanResponseDto
	 * @see MemberRepository
	 * @see GroupRepository
	 * @see FundService
	 * @see AccountRepository
	 * @see FundManagementRepository
	 * @see TransferService
	 */
	@Transactional
	@Override
	public PostRepaidLoanResponseDto repaidLoan(Long memberId, PostCommonLoanRequestDto postCommonLoanRequestDto) {
		Member child = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		Group group = groupRepository.findByChild(child)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_CHILD));

		// 이자 존재 시 중도 상환 X
		GetInterestResponseDto getInterestResponseDto = fundService.getInterest(memberId, group.getId());
		if (getInterestResponseDto.getAmount() != 0 || getInterestResponseDto.getOverdueAmount() != 0) {
			throw new LoanException(LoanErrorInfo.NOT_PAID_INTEREST);
		}

		Account loanAccount = accountRepository.findByMemberAndType(child, AccountType.LOAN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_LOAN_ACCOUNT));

		Account mainAccount = accountRepository.findByMemberAndType(child, AccountType.MAIN_ACCOUNT)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.NOT_FOUND_ACCOUNT));

		FundManagement fundManagement = fundManagementRepository.findByGroup(group)
			.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP));

		if (postCommonLoanRequestDto.getAmount() > fundManagement.getLoanAmount()) {
			throw new LoanException(LoanErrorInfo.LOAN_BALANCE_REPAID_AMOUNT_EXCEEDED);
		}

		// 땡겨쓰기 중도 상환
		TransferRequestDto transferRequestDto = TransferRequestDto.of(mainAccount, loanAccount,
			postCommonLoanRequestDto.getAmount(), TransactionType.LOAN);
		TransferResponseDto response = transferService.transfer(transferRequestDto);
		fundManagement.minusLoanAmount(response.getAmount());

		return PostRepaidLoanResponseDto.of(transferRequestDto.getAmount(), fundManagement.getLoanAmount());
	}

	/**
	 *  이자를 생성하는 메소드, Cron
	 *
	 * @see InterestRepository
	 * @see ConfiscationRepository
	 * @see FundManagementRepository
	 * @see NotificationService
	 */
	@Transactional
	@Override
	public void createInterestByCron() {
		// TODO: 매일 오전 00시에 CRON 동작
		LocalDate today = LocalDate.now();
		List<Interest> interests = interestRepository.findAllByBilledAtAndActivatedFalse(today);
		for (Interest interest : interests) {
			Group group = interest.getGroup();
			Member child = group.getChild();

			Optional<Interest> oldInterest = interestRepository.findByGroupAndActivatedFalseAndBilledAtGreaterThan(
				group, today);

			if (oldInterest.isEmpty()) {
				interestRepository.save(Interest.of(group));
			}

			// 현재 압류 중인지 확인
			if (confiscationRepository.existsByGroupIdAndActivatedTrue(group.getId())) {
				continue;
			}

			FundManagement fundManagement = fundManagementRepository.findByGroup(group)
				.orElseThrow(() -> new FundException(FundErrorInfo.NOT_FOUND_FUND_MANAGEMENT_BY_GROUP));

			interest.updateAmount(fundManagement.getLoanAmount(),
				InterestRate.ofRating(CreditRating.getCreditRating(child.getCreditScore()).getRating()));
			interest.updateActivated(true);

			notificationService.saveNotification(
				CreateNotificationDto.of(String.format("이자 %s원이 발생했습니다.",
						interest.getAmount().toString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",")),
					child, NotificationType.INTEREST));
		}
	}

}
