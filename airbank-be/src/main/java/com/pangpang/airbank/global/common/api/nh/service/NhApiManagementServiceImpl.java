package com.pangpang.airbank.global.common.api.nh.service;

import org.springframework.stereotype.Service;

import com.pangpang.airbank.global.common.api.nh.domain.NhApiManagement;
import com.pangpang.airbank.global.common.api.nh.repository.NhApiManagementRepository;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NhApiManagementServiceImpl implements NhApiManagementService {
	private final NhApiManagementRepository nhApiManagementRepository;

	@Override
	public Long updateIsTuno() {
		NhApiManagement nhApiManagement = nhApiManagementRepository.findById(1L)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.ACCOUNT_REQUEST_DATA_ERROR));

		nhApiManagement.updateIsTuno();
		return nhApiManagement.getIsTuno();
	}
}
