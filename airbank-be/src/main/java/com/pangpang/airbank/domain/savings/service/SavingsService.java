package com.pangpang.airbank.domain.savings.service;

import com.pangpang.airbank.domain.group.dto.CommonIdResponseDto;
import com.pangpang.airbank.domain.savings.dto.GetCurrentSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchCancelSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PatchCommonSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchConfirmSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostSaveSavingsRequestDto;

public interface SavingsService {
	GetCurrentSavingsResponseDto getCurrentSavings(Long groupId);

	CommonIdResponseDto saveSavings(Long memberId, PostSaveSavingsRequestDto postSaveSavingsRequestDto);

	PatchCommonSavingsResponseDto confirmEnrollmentSavings(Long memberId,
		PatchConfirmSavingsRequestDto patchConfirmSavingsRequestDto,
		Long groupId);

	PatchCommonSavingsResponseDto cancelSavings(Long memberId,
		PatchCancelSavingsRequestDto patchCancelSavingsRequestDto);
}
