package com.pangpang.airbank.domain.member.service;

import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PostLoginRequestDto;


public interface MemberService {

	GetMemberResponseDto getMember(Long memberId);

	Member getMemberByOauthIdentifier(PostLoginRequestDto postLoginRequestDto);

	Member saveMember(PostLoginRequestDto postLoginRequestDto);
}
