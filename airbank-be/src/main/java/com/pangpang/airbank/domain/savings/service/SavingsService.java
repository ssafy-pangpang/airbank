package com.pangpang.airbank.domain.savings.service;

import com.pangpang.airbank.domain.savings.dto.GetCurrentSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchCancelSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PatchCommonSavingsResponseDto;
import com.pangpang.airbank.domain.savings.dto.PatchConfirmSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostRewardSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostSaveSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostTransferSavingsRequestDto;
import com.pangpang.airbank.global.common.response.CommonAmountResponseDto;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;

public interface SavingsService {
	GetCurrentSavingsResponseDto getCurrentSavings(Long groupId);

	CommonIdResponseDto saveSavings(Long memberId, PostSaveSavingsRequestDto postSaveSavingsRequestDto);

	PatchCommonSavingsResponseDto confirmEnrollmentSavings(Long memberId,
		PatchConfirmSavingsRequestDto patchConfirmSavingsRequestDto,
		Long groupId);

	PatchCommonSavingsResponseDto cancelSavings(Long memberId,
		PatchCancelSavingsRequestDto patchCancelSavingsRequestDto);

	CommonAmountResponseDto transferSavings(Long memberId, PostTransferSavingsRequestDto postTransferSavingsRequestDto);

	CommonAmountResponseDto rewardSavings(Long memberId, PostRewardSavingsRequestDto postRewardSavingsRequestDto,
		Long groupId);
}
