package com.pangpang.airbank.domain.member.service;

import com.pangpang.airbank.domain.auth.dto.GetLogoutResponseDto;
import com.pangpang.airbank.domain.auth.dto.PostLoginRequestDto;
import com.pangpang.airbank.domain.member.dto.GetCreditHistoryResponseDto;
import com.pangpang.airbank.domain.member.dto.GetCreditResponseDto;
import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.LoginMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberRequestDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberResponseDto;

public interface MemberService {

	GetMemberResponseDto getMember(Long memberId);

	LoginMemberResponseDto getMemberByOauthIdentifier(PostLoginRequestDto postLoginRequestDto);

	LoginMemberResponseDto saveMember(PostLoginRequestDto postLoginRequestDto);

	GetLogoutResponseDto getMemberName(Long memberId);

	PatchMemberResponseDto updateMember(Long memberId, PatchMemberRequestDto patchMemberRequestDto);

	Boolean isValidMember(Long memberId);

	void updateCreditScore(Long memberId, Integer points);

	void updateCreditScoreByRate(Long memberId, Double rate);

	GetCreditResponseDto getCreditRating(Long memberId, Long groupId);

	GetCreditHistoryResponseDto getCreditHistory(Long memberId, Long groupId);
}
