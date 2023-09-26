package com.pangpang.airbank.domain.account.service;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.dto.DepositTransferRequestDto;
import com.pangpang.airbank.domain.account.dto.SaveDepositHistoryRequestDto;
import com.pangpang.airbank.domain.account.dto.SaveWithdrawalHistoryRequestDto;
import com.pangpang.airbank.domain.account.dto.TransferRequestDto;
import com.pangpang.airbank.domain.account.dto.TransferResponseDto;
import com.pangpang.airbank.domain.account.dto.WithdrawalTransferRequestDto;
import com.pangpang.airbank.domain.account.repository.AccountRepository;
import com.pangpang.airbank.global.common.api.nh.NHApi;
import com.pangpang.airbank.global.common.api.nh.dto.PostDepositTransferResponseDto;
import com.pangpang.airbank.global.common.api.nh.dto.PostWithdrawalTransferResponseDto;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;
import com.pangpang.airbank.global.meta.domain.AccountType;
import com.pangpang.airbank.global.meta.domain.TransactionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
	private static final String NORMAL_PROCESSING_MESSAGE = "정상처리 되었습니다.";

	private final NHApi nhApi;
	private final AccountHistoryService accountHistoryService;

	@Override
	@Transactional
	public TransferResponseDto transfer(TransferRequestDto transferRequestDto) {
		withdraw(transferRequestDto);
		return deposit(transferRequestDto);
	}

	private void withdraw(TransferRequestDto transferRequestDto) {
		UUID transactionIdentifier = accountHistoryService.saveWithdrawalHistory(
			SaveWithdrawalHistoryRequestDto.from(transferRequestDto));

		PostWithdrawalTransferResponseDto postWithdrawalTransferResponseDto;

		try {
			postWithdrawalTransferResponseDto = nhApi.withdrawalTransfer(
				WithdrawalTransferRequestDto.of(transferRequestDto, uuidToBase64(transactionIdentifier)));
		} catch (Exception e) {
			throw new AccountException(AccountErrorInfo.ACCOUNT_NH_SERVER_ERROR);
		}

		if (!postWithdrawalTransferResponseDto.getHeader().getRsms().equals(NORMAL_PROCESSING_MESSAGE)) {
			throw new AccountException(AccountErrorInfo.ACCOUNT_NH_SERVER_ERROR);
		}

	}

	private TransferResponseDto deposit(TransferRequestDto transferRequestDto) {
		UUID transactionIdentifier = accountHistoryService.saveDepositHistory(
			SaveDepositHistoryRequestDto.from(transferRequestDto));

		PostDepositTransferResponseDto postDepositTransferResponseDto;

		try {
			postDepositTransferResponseDto = nhApi.depositTransfer(
				DepositTransferRequestDto.of(transferRequestDto, uuidToBase64(transactionIdentifier)));
		} catch (Exception e) {
			throw new AccountException(AccountErrorInfo.ACCOUNT_NH_SERVER_ERROR);
		}

		if (!postDepositTransferResponseDto.getHeader().getRsms().equals(NORMAL_PROCESSING_MESSAGE)) {
			throw new AccountException(AccountErrorInfo.ACCOUNT_NH_SERVER_ERROR);
		}

		return TransferResponseDto.from(transferRequestDto.getAmount());
	}

	private String uuidToBase64(UUID uuid) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());
		return Base64.encodeBase64URLSafeString(byteBuffer.array());
	}
}
