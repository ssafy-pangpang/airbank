package com.pangpang.airbank.domain.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.auth.dto.GetLoginResponseDto;
import com.pangpang.airbank.domain.auth.dto.GetLogoutResponseDto;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.dto.GetLoginMemberResponseDto;
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
	public GetLoginMemberResponseDto getMemberByOauthIdentifier(PostLoginRequestDto postLoginRequestDto) {
		Optional<Member> optionalMember = memberRepository.findByOauthIdentifier(postLoginRequestDto.getId());

		if (optionalMember.isPresent()) {
			return GetLoginMemberResponseDto.from(optionalMember.get().getId(),
				new GetLoginResponseDto(optionalMember.get().getName(), optionalMember.get().getPhoneNumber()));
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
	public GetLoginMemberResponseDto saveMember(PostLoginRequestDto postLoginRequestDto) {
		Member member = memberRepository.save(Member.of(postLoginRequestDto));
		return GetLoginMemberResponseDto.from(member.getId(),
			new GetLoginResponseDto(member.getName(), member.getPhoneNumber()));
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
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		return member.getOauthIdentifier();
	}

	/**
	 *  사용자 id 조회
	 *
	 * @param Long memberId
	 * @return 사용자 id
	 */
	@Transactional(readOnly = true)
	@Override
	public GetLogoutResponseDto getMemberName(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		return new GetLogoutResponseDto(member.getName());
	}

}
