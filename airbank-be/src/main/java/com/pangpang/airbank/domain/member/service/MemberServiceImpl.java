package com.pangpang.airbank.domain.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.auth.dto.GetLogoutResponseDto;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.dto.GetLoginMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberRequestDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PostLoginRequestDto;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;
import com.pangpang.airbank.global.meta.domain.CreditRating;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final CreditHistoryService creditHistoryService;

	/**
	 *  사용자 조회
	 *
	 * @param Long memberId
	 * @return 사용자 정보
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
	 * @param PostLoginRequestDto postLoginRequestDto
	 * @return 사용자 정보
	 */
	@Transactional(readOnly = true)
	@Override
	public GetLoginMemberResponseDto getMemberByOauthIdentifier(PostLoginRequestDto postLoginRequestDto) {
		Optional<Member> optionalMember = memberRepository.findByOauthIdentifier(postLoginRequestDto.getId());

		if (optionalMember.isPresent()) {
			return GetLoginMemberResponseDto.from(optionalMember.get());
		}
		return saveMember(postLoginRequestDto);
	}

	/**
	 *  회원가입
	 *
	 * @param PostLoginRequestDto postLoginRequestDto
	 * @return 가입한 사용자
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public GetLoginMemberResponseDto saveMember(PostLoginRequestDto postLoginRequestDto) {
		Member member = memberRepository.save(Member.from(postLoginRequestDto));
		creditHistoryService.saveCreditHistory(member);
		return GetLoginMemberResponseDto.from(member);
	}

	/**
	 *  사용자 oauth식별자 조회
	 *
	 * @param Long memberId
	 * @return 사용자의 oauthIdentifier
	 */
	@Transactional(readOnly = true)
	@Override
	public String getMemberOauthIdentifier(Long memberId) {
		Member member = getMemberByIdOrElseThrowException(memberId);
		return member.getOauthIdentifier();
	}

	/**
	 *  사용자 이름 조회
	 *
	 * @param Long memberId
	 * @return 사용자 id
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
	 * @param Long memberId
	 *        PatchMemberRequestDto patchMemberRequestDto
	 * @return 수정 후의 정보
	 */
	@Transactional
	@Override
	public PatchMemberResponseDto updateMember(Long memberId, PatchMemberRequestDto patchMemberRequestDto) {
		Member member = getMemberByIdOrElseThrowException(memberId);
		if (patchMemberRequestDto.getName() != null) {
			member.setName(patchMemberRequestDto.getName());
		}
		if (patchMemberRequestDto.getPhoneNumber() != null) {
			member.setPhoneNumber(patchMemberRequestDto.getPhoneNumber());
		}
		if (patchMemberRequestDto.getRole() != null) {
			member.setRole(patchMemberRequestDto.getRole());
		}
		return PatchMemberResponseDto.from(member);
	}

	/**
	 *  사용자 조회 (내부 로직)
	 *
	 * @param Long memberId
	 * @return 사용자 객체
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

	/*
	 *  신용점수 수정
	 *
	 * @param memberId Long
	 *        points Integer
	 * @return void
	 */
	@Override
	public void updateCreditScore(Long memberId, Integer points) {
		Member member = getMemberByIdOrElseThrowException(memberId);
		Integer newCreditScore = member.getCreditScore() + points;

		if (newCreditScore > CreditRating.ONE.getMaxScore()) {
			newCreditScore = CreditRating.ONE.getMaxScore();
		}
		if (newCreditScore < CreditRating.TEN.getMinScore()) {
			newCreditScore = CreditRating.TEN.getMinScore();
		}

		member.setCreditScore(newCreditScore);
		creditHistoryService.saveCreditHistory(member);
	}

	/**
	 *  신용점수 비율로 수정
	 *
	 * @param memberId Long
	 *        rate Double
	 * @return void
	 */
	@Override
	public void updateCreditScoreByRate(Long memberId, Double rate) {
		Member member = getMemberByIdOrElseThrowException(memberId);
		CreditRating creditRating = CreditRating.getCreditRating(member.getCreditScore());
		Integer points = Integer.valueOf(
			(int)Math.round((creditRating.getMaxScore() - creditRating.getMinScore()) * rate));
		Integer newCreditScore = member.getCreditScore() + points;

		if (newCreditScore > CreditRating.ONE.getMaxScore()) {
			newCreditScore = CreditRating.ONE.getMaxScore();
		}
		if (newCreditScore < CreditRating.TEN.getMinScore()) {
			newCreditScore = CreditRating.TEN.getMinScore();
		}
		
		member.setCreditScore(newCreditScore);
		creditHistoryService.saveCreditHistory(member);
	}
}
