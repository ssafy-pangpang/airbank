package com.pangpang.airbank.global.common.api.nh.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.global.common.api.nh.domain.NhApiManagement;
import com.pangpang.airbank.global.common.api.nh.repository.NhApiManagementRepository;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NhApiManagementServiceImpl implements NhApiManagementService {
	private final NhApiManagementRepository nhApiManagementRepository;

	/**
	 *  NH API Management의 IsTuno 값을 1씩 증가.
	 *  다른 트랜젝션과 분리되어 실행
	 *
	 * @return Long
	 * @see NhApiManagement
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = RuntimeException.class)
	public Long updateIsTuno() {
		NhApiManagement nhApiManagement = nhApiManagementRepository.findById(1L)
			.orElseThrow(() -> new AccountException(AccountErrorInfo.ACCOUNT_REQUEST_DATA_ERROR));
		nhApiManagement.updateIsTuno();
		return nhApiManagement.getIsTuno();
	}
}
