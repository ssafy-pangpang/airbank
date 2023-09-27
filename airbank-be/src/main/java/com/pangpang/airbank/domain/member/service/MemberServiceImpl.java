package com.pangpang.airbank.domain.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import com.pangpang.airbank.global.meta.domain.CreditRating;
import com.pangpang.airbank.global.meta.domain.MemberRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;
	private final GroupRepository groupRepository;
	private final CreditHistoryRepository creditHistoryRepository;
	private final CreditHistoryService creditHistoryService;

	/**
	 *  사용자 조회
	 *
	 * @param memberId Long
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
	 * @param postLoginRequestDto PostLoginRequestDto
	 * @return 사용자 정보
	 */
	@Transactional(readOnly = true)
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
	 * @return 가입한 사용자
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public LoginMemberResponseDto saveMember(PostLoginRequestDto postLoginRequestDto) {
		Member member = memberRepository.save(Member.from(postLoginRequestDto));
		creditHistoryService.saveCreditHistory(member);
		return LoginMemberResponseDto.from(member);
	}

	/**
	 *  사용자 이름 조회
	 *
	 * @param memberId Long
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
	 * @param memberId Long
	 *        patchMemberRequestDto PatchMemberRequestDto
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
			isDuplicatePhoneNumber(patchMemberRequestDto.getPhoneNumber());
			member.setPhoneNumber(patchMemberRequestDto.getPhoneNumber());
		}
		if (patchMemberRequestDto.getRole() != null) {
			member.setRole(patchMemberRequestDto.getRole());
		}
		return PatchMemberResponseDto.from(member);
	}

	/**
	 *  휴대폰 번호 중복 확인
	 *
	 * @param phoneNumber String
	 * @return 이미 가입된 휴대폰 번호일 경우 예외 발생
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

	/**
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

	/**
	 *  신용 등급 조회
	 *
	 * @param memberId Long
	 * @return 신용 등급
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
	 *        groupId Long
	 * @return 신용점수 변동 내역 리스트
	 * @see com.pangpang.airbank.domain.member.dto.CreditHistoryElement
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
	 * @return 자녀 정보
	 */
	private Member getChildInGroup(Long groupId) {
		Group group = groupRepository.findById(groupId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_ID));

		return group.getChild();
	}
}
