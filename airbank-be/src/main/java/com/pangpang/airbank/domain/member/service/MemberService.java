package com.pangpang.airbank.domain.member.service;

import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;

public interface MemberService {

	GetMemberResponseDto getMember(Long memberId);
}
