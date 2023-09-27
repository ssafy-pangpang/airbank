package com.pangpang.airbank.domain.group.service;

import com.pangpang.airbank.domain.group.dto.CommonFundManagementRequestDto;
import com.pangpang.airbank.domain.group.dto.GetPartnersResponseDto;
import com.pangpang.airbank.domain.group.dto.PatchConfirmChildRequestDto;
import com.pangpang.airbank.domain.group.dto.PatchFundManagementResponseDto;
import com.pangpang.airbank.domain.group.dto.PostEnrollChildRequestDto;
import com.pangpang.airbank.global.common.response.CommonIdResponseDto;

public interface GroupService {
	GetPartnersResponseDto getPartners(Long memberId);

	CommonIdResponseDto enrollChild(Long memberId, PostEnrollChildRequestDto postEnrollChildRequestDto);

	CommonIdResponseDto confirmEnrollmentChild(Long memberId, PatchConfirmChildRequestDto patchConfirmChildRequestDto,
		Long groupId);

	CommonIdResponseDto saveFundManagement(Long memberId,
		CommonFundManagementRequestDto commonFundManagementRequestDto, Long groupId);

	PatchFundManagementResponseDto updateFundManagement(Long memberId,
		CommonFundManagementRequestDto commonFundManagementRequestDto, Long groupId);

	Boolean isMemberInGroup(Long memberId, Long groupId);
}
