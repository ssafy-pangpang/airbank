package com.pangpang.airbank.domain.group.service;

import com.pangpang.airbank.domain.group.dto.GetPartnersResponseDto;

public interface GroupService {
	GetPartnersResponseDto getPartners(Long memberId);
}
