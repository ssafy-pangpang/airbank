package com.pangpang.airbank.domain.account.service;

import com.pangpang.airbank.domain.account.dto.TransferRequestDto;
import com.pangpang.airbank.domain.account.dto.TransferResponseDto;

public interface TransferService {
	TransferResponseDto transfer(TransferRequestDto transferRequestDto);
}
