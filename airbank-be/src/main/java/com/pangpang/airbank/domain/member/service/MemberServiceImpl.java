package com.pangpang.airbank.domain.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PostLoginRequestDto;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepository;

	/**
	 *  사용자 조회
	 * 
	 * @param Long memberId
	 * @return 사용자 정보
	 */
	@Transactional(readOnly = true)
	@Override
	public GetMemberResponseDto getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

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
	public Member getMemberByOauthIdentifier(PostLoginRequestDto postLoginRequestDto) {
		Optional<Member> optionalMember = memberRepository.findByOauthIdentifier(postLoginRequestDto.getId());

		if (optionalMember.isPresent()) {
			return optionalMember.get();
		} else {
			return saveMember(postLoginRequestDto);
		}
	}

	/**
	 *  회원가입
	 *
	 * @param PostLoginRequestDto postLoginRequestDto
	 * @return 가입한 사용자
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public Member saveMember(PostLoginRequestDto postLoginRequestDto) {
		Member member = Member.of(postLoginRequestDto);
		memberRepository.save(member);
		return member;
	}

}
