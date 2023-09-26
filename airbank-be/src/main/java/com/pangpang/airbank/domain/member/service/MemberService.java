package com.pangpang.airbank.domain.member.service;

import com.pangpang.airbank.domain.auth.dto.GetLogoutResponseDto;
import com.pangpang.airbank.domain.member.dto.GetCreditHistoryResponseDto;
import com.pangpang.airbank.domain.member.dto.GetCreditResponseDto;
import com.pangpang.airbank.domain.member.dto.GetLoginMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.GetMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberRequestDto;
import com.pangpang.airbank.domain.member.dto.PatchMemberResponseDto;
import com.pangpang.airbank.domain.member.dto.PostLoginRequestDto;

public interface MemberService {

	GetMemberResponseDto getMember(Long memberId);

	GetLoginMemberResponseDto getMemberByOauthIdentifier(PostLoginRequestDto postLoginRequestDto);

	GetLoginMemberResponseDto saveMember(PostLoginRequestDto postLoginRequestDto);

	String getMemberOauthIdentifier(Long memberId);

	GetLogoutResponseDto getMemberName(Long memberId);

	PatchMemberResponseDto updateMember(Long memberId, PatchMemberRequestDto patchMemberRequestDto);

	Boolean isValidMember(Long memberId);

	void updateCreditScore(Long memberId, Integer points);

	void updateCreditScoreByRate(Long memberId, Double rate);

	GetCreditResponseDto getCreditRating(Long memberId, Long groupId);

	GetCreditHistoryResponseDto getCreditHistory(Long memberId, Long groupId);
}
