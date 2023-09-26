package com.pangpang.airbank.domain.account.service;

import com.pangpang.airbank.domain.account.dto.TransferRequestDto;

public interface TransferService {
	void transfer(TransferRequestDto transferRequestDto);
}
