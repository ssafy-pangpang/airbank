package com.pangpang.airbank.domain.group.service;

import com.pangpang.airbank.domain.group.dto.CommonFundManagementRequestDto;
import com.pangpang.airbank.domain.group.dto.CommonIdResponseDto;
import com.pangpang.airbank.domain.group.dto.GetPartnersResponseDto;
import com.pangpang.airbank.domain.group.dto.PatchConfirmRequestDto;
import com.pangpang.airbank.domain.group.dto.PatchFundManagementResponseDto;
import com.pangpang.airbank.domain.group.dto.PostEnrollChildRequestDto;

public interface GroupService {
	GetPartnersResponseDto getPartners(Long memberId);

	CommonIdResponseDto enrollChild(Long memberId, PostEnrollChildRequestDto postEnrollChildRequestDto);

	CommonIdResponseDto confirmEnrollment(Long memberId, PatchConfirmRequestDto patchConfirmRequestDto, Long groupId);

	CommonIdResponseDto saveFundManagement(Long memberId,
		CommonFundManagementRequestDto commonFundManagementRequestDto, Long groupId);

	PatchFundManagementResponseDto updateFundManagement(Long memberId,
		CommonFundManagementRequestDto commonFundManagementRequestDto, Long groupId);
}
