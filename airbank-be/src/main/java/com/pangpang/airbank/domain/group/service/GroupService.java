package com.pangpang.airbank.domain.group.service;

import com.pangpang.airbank.domain.group.dto.GetPartnersResponseDto;
import com.pangpang.airbank.domain.group.dto.PostEnrollChildRequestDto;

public interface GroupService {
	GetPartnersResponseDto getPartners(Long memberId);

	Long enrollChild(Long memberId, PostEnrollChildRequestDto postEnrollChildRequestDto);
}
