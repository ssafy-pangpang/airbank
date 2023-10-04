package com.pangpang.airbank.domain.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.account.service.AccountService;
import com.pangpang.airbank.domain.auth.dto.GetLogoutResponseDto;
import com.pangpang.airbank.domain.auth.dto.PostLoginRequestDto;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.group.repository.GroupRepository;
import com.pangpang.airbank.domain.member.domain.CreditHistory;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.dto.GetCreditHistoryResponseDto;
import com.pangpang.airbank.domain.member.dto.GetCreditResponseDto;
import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.LoginMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberRequestDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberResponseDto;
import com.pangpang.airbank.domain.member.repository.CreditHistoryRepository;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.meta.domain.AccountType;
import com.pangpang.airbank.global.meta.domain.CreditRating;
import com.pangpang.airbank.global.meta.domain.MemberRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final GroupRepository groupRepository;
	private final CreditHistoryRepository creditHistoryRepository;
	private final AccountService accountService;

	/**
	 *  사용자 조회
	 *
	 * @param memberId Long
	 * @return GetMemberResponseDto
	 */
	@Transactional(readOnly = true)
	@Override
	public GetMemberResponseDto getMember(Long memberId) {
		Member member = getMemberByIdOrElseThrowException(memberId);
		return GetMemberResponseDto.from(member);
	}

	/**
	 *  카카오 식별자로 사용자 조회
	 *
	 * @param postLoginRequestDto PostLoginRequestDto
	 * @return LoginMemberResponseDto
	 * @see MemberRepository
	 */
	@Transactional
	@Override
	public LoginMemberResponseDto getMemberByOauthIdentifier(PostLoginRequestDto postLoginRequestDto) {
		Optional<Member> optionalMember = memberRepository.findByOauthIdentifier(
			postLoginRequestDto.getOauthIdentifier());

		if (optionalMember.isPresent()) {
			return LoginMemberResponseDto.from(optionalMember.get());
		}
		return saveMember(postLoginRequestDto);
	}

	/**
	 *  회원가입
	 *
	 * @param postLoginRequestDto PostLoginRequestDto
	 * @return LoginMemberResponseDto
	 * @see MemberRepository
	 * @see CreditHistoryRepository
	 * @see AccountService
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public LoginMemberResponseDto saveMember(PostLoginRequestDto postLoginRequestDto) {
		Member member = memberRepository.save(Member.from(postLoginRequestDto));

		// 신용점수 생성
		creditHistoryRepository.save(CreditHistory.from(member));
		// 메인 계좌 생성
		accountService.saveVirtualAccount(member.getId(), AccountType.MAIN_ACCOUNT);

		return LoginMemberResponseDto.from(member);
	}

	/**
	 *  사용자 이름 조회
	 *
	 * @param memberId Long
	 * @return GetLogoutResponseDto
	 */
	@Transactional(readOnly = true)
	@Override
	public GetLogoutResponseDto getMemberName(Long memberId) {
		Member member = getMemberByIdOrElseThrowException(memberId);
		return new GetLogoutResponseDto(member);
	}

	/**
	 *  사용자 정보 수정
	 *
	 * @param memberId Long
	 * @param patchMemberRequestDto PatchMemberRequestDto
	 * @return PatchMemberResponseDto
	 */
	@Transactional
	@Override
	public PatchMemberResponseDto updateMember(Long memberId, PatchMemberRequestDto patchMemberRequestDto) {
		Member member = getMemberByIdOrElseThrowException(memberId);
		if (patchMemberRequestDto.getName() != null) {
			member.setName(patchMemberRequestDto.getName());
		}
		if (patchMemberRequestDto.getPhoneNumber() != null) {
			isDuplicatePhoneNumber(patchMemberRequestDto.getPhoneNumber());
			member.setPhoneNumber(patchMemberRequestDto.getPhoneNumber());
		}
		if (patchMemberRequestDto.getRole() != null) {
			member.setRole(MemberRole.ofName(patchMemberRequestDto.getRole()));
		}
		return PatchMemberResponseDto.from(member);
	}

	/**
	 *  휴대폰 번호 중복 확인
	 *
	 * @param phoneNumber String
	 */
	private void isDuplicatePhoneNumber(String phoneNumber) {
		if (memberRepository.existsByPhoneNumber(phoneNumber)) {
			throw new MemberException(MemberErrorInfo.DUPLICATE_PHONENUMBER);
		}
	}

	/**
	 *  사용자 조회 (내부 로직)
	 *
	 * @param memberId Long
	 * @return Member
	 * @see MemberRepository
	 */
	private Member getMemberByIdOrElseThrowException(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		return member;
	}

	/**
	 *  memberId 검증
	 *
	 * @param memberId Long
	 * @return Boolean
	 * @see MemberRepository
	 */
	@Transactional(readOnly = true)
	@Override
	public Boolean isValidMember(Long memberId) {
		return memberRepository.existsById(memberId);
	}

	/**
	 *  신용점수 수정
	 *
	 * @param childId Long
	 * @param points Integer
	 * @see CreditRating
	 * @see CreditHistoryRepository
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateCreditScoreByPoints(Long childId, Integer points) {
		if (points == 0) {
			throw new MemberException(MemberErrorInfo.NOT_FOUND_UPDATE_CREDIT_POINTS);
		}

		Member member = getMemberByIdOrElseThrowException(childId);

		if (member.getCreditScore().equals(CreditRating.ONE.getMaxScore()) && (points > 0)) {
			return;
		}
		if (member.getCreditScore().equals(CreditRating.TEN.getMinScore()) && (points < 0)) {
			return;
		}

		member.updateCreditScore(points);

		creditHistoryRepository.save(CreditHistory.from(member));
	}

	/**
	 *  신용점수 비율로 수정
	 *
	 * @param childId Long
	 * @param rate Double
	 * @see CreditRating
	 * @see CreditHistoryRepository
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void updateCreditScoreByRate(Long childId, Double rate) {
		if (Double.compare(rate, 0D) == 0) {
			throw new MemberException(MemberErrorInfo.NOT_FOUND_UPDATE_CREDIT_RATE);
		}

		Member member = getMemberByIdOrElseThrowException(childId);

		if (member.getCreditScore().equals(CreditRating.ONE.getMaxScore()) && (Double.compare(rate, 0D) > 0)) {
			return;
		}
		if (member.getCreditScore().equals(CreditRating.TEN.getMinScore()) && (Double.compare(rate, 0D) < 0)) {
			return;
		}

		CreditRating creditRating = CreditRating.getCreditRating(member.getCreditScore());

		Integer points = Integer.valueOf(
			(int)Math.round((creditRating.getMaxScore() - creditRating.getMinScore()) * rate));

		member.updateCreditScore(points);

		creditHistoryRepository.save(CreditHistory.from(member));
	}

	/**
	 *  신용 등급 조회
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetCreditResponseDto
	 * @see CreditRating
	 */
	@Transactional(readOnly = true)
	@Override
	public GetCreditResponseDto getCreditRating(Long memberId, Long groupId) {
		Member member = getMemberByIdOrElseThrowException(memberId);

		if (member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			member = getChildInGroup(groupId);
		}

		return new GetCreditResponseDto(CreditRating.getCreditRating(member.getCreditScore()).getRating());
	}

	/**
	 *  신용점수 변동 내역 조회
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetCreditHistoryResponseDto
	 * @see CreditHistoryRepository
	 */
	@Transactional(readOnly = true)
	@Override
	public GetCreditHistoryResponseDto getCreditHistory(Long memberId, Long groupId) {
		Member member = getMemberByIdOrElseThrowException(memberId);

		if (member.getRole().getName().equals(MemberRole.PARENT.getName())) {
			member = getChildInGroup(groupId);
		}

		List<CreditHistory> creditHistories = creditHistoryRepository.findAllByMemberId(member.getId());

		return GetCreditHistoryResponseDto.from(creditHistories);
	}

	/**
	 *  그룹에 속한 자녀 조회
	 *
	 * @param groupId Long
	 * @return Member
	 * @see GroupRepository
	 */
	private Member getChildInGroup(Long groupId) {
		Group group = groupRepository.findById(groupId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_ID));

		return group.getChild();
	}
}
