package com.pangpang.airbank.global.common.api.nh;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pangpang.airbank.domain.account.dto.DepositTransferRequestDto;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.domain.account.dto.WithdrawalTransferRequestDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetCheckFinAccountRequestDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetCheckFinAccountResponseDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetFinAccountRequestDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetFinAccountResponseDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetInquireBalanceRequestDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetInquireBalanceResponseDto;
import com.pangpang.airbank.global.common.api.nh.dto.PostDepositTransferRequestDto;
import com.pangpang.airbank.global.common.api.nh.dto.PostDepositTransferResponseDto;
import com.pangpang.airbank.global.common.api.nh.dto.PostWithdrawalTransferRequestDto;
import com.pangpang.airbank.global.common.api.nh.dto.PostWithdrawalTransferResponseDto;
import com.pangpang.airbank.global.common.api.nh.service.NhApiManagementService;
import com.pangpang.airbank.global.error.exception.AccountException;
import com.pangpang.airbank.global.error.info.AccountErrorInfo;

import lombok.RequiredArgsConstructor;

/**
 *  NH API 연동 함수
 *
 * @See NHApiConstantProvider
 * @See NhApiManagementRepository
 * @See ObjectMapper
 */
@Component
@RequiredArgsConstructor
public class NHApi {
	private final NhApiManagementService nhApiManagementService;
	private final NHApiConstantProvider nhApiConstantProvider;
	private final ObjectMapper objectMapper;

	/**
	 *  계좌 핀-어카운트 발급받는 API 호출
	 *
	 * @param postEnrollAccountRequestDto PostEnrollAccountRequestDto
	 * @return GetFinAccountResponseDto
	 * @see GetFinAccountRequestDto
	 */
	public GetFinAccountResponseDto getFinAccountDirect(PostEnrollAccountRequestDto postEnrollAccountRequestDto) throws
		JsonProcessingException {
		String result = WebClient.create()
			.post()
			.uri(nhApiConstantProvider.getUrl() + "/OpenFinAccountDirect.nh")
			.header("Content-type", "application/json;charset=utf-8")
			.bodyValue(
				objectMapper.writeValueAsString(GetFinAccountRequestDto.of(nhApiConstantProvider,
					nhApiManagementService.updateIsTuno(), postEnrollAccountRequestDto)))
			.retrieve()
			.bodyToMono(String.class)
			.block();

		GetFinAccountResponseDto response = objectMapper.readValue(result, GetFinAccountResponseDto.class);

		if (!response.getHeader().getRsms().contains(nhApiConstantProvider.getNormalProcessingMessage())) {
			throw new AccountException(AccountErrorInfo.ACCOUNT_ENROLL_ERROR);
		}
		return response;
	}

	/**
	 *  핀-어카운트 발급 확인
	 *
	 * @param rgno String
	 * @return GetCheckFinAccountResponseDto
	 * @see NHApiConstantProvider
	 * @see GetCheckFinAccountRequestDto
	 */
	public GetCheckFinAccountResponseDto checkOpenFinAccountDirect(String rgno) throws JsonProcessingException {
		String result = WebClient.create()
			.post()
			.uri(nhApiConstantProvider.getUrl() + "/CheckOpenFinAccountDirect.nh")
			.header("Content-type", "application/json;charset=utf-8")
			.bodyValue(
				objectMapper.writeValueAsString(GetCheckFinAccountRequestDto.of(nhApiConstantProvider,
					nhApiManagementService.updateIsTuno(), rgno)))
			.retrieve()
			.bodyToMono(String.class)
			.block();

		GetCheckFinAccountResponseDto response = objectMapper.readValue(result, GetCheckFinAccountResponseDto.class);

		if (!response.getHeader().getRsms().contains(nhApiConstantProvider.getNormalProcessingMessage())) {
			throw new AccountException(AccountErrorInfo.ACCOUNT_ENROLL_ERROR);
		}
		return response;
	}

	/**
	 * 출금이체
	 *
	 * @param withdrawalTransferRequestDto
	 * @return PostWithdrawalTransferResponseDto
	 * @see WithdrawalTransferRequestDto
	 * @see PostWithdrawalTransferRequestDto
	 */
	public PostWithdrawalTransferResponseDto withdrawalTransfer(
		WithdrawalTransferRequestDto withdrawalTransferRequestDto) throws JsonProcessingException {
		String result = WebClient.create()
			.post()
			.uri(nhApiConstantProvider.getUrl() + "/DrawingTransfer.nh")
			.header("Content-type", "application/json;charset=utf-8")
			.bodyValue(
				objectMapper.writeValueAsString(PostWithdrawalTransferRequestDto.of(nhApiConstantProvider,
					nhApiManagementService.updateIsTuno(), withdrawalTransferRequestDto)))
			.retrieve()
			.bodyToMono(String.class)
			.block();
		return objectMapper.readValue(result, PostWithdrawalTransferResponseDto.class);
	}

	/**
	 * 농협 입금이체
	 *
	 * @param depositTransferRequestDto
	 * @return PostDepositTransferResponseDto
	 * @see DepositTransferRequestDto
	 * @see PostDepositTransferRequestDto
	 */
	public PostDepositTransferResponseDto depositTransfer(
		DepositTransferRequestDto depositTransferRequestDto) throws JsonProcessingException {
		String result = WebClient.create()
			.post()
			.uri(nhApiConstantProvider.getUrl() + "/ReceivedTransferAccountNumber.nh")
			.header("Content-type", "application/json;charset=utf-8")
			.bodyValue(
				objectMapper.writeValueAsString(PostDepositTransferRequestDto.of(nhApiConstantProvider,
					nhApiManagementService.updateIsTuno(), depositTransferRequestDto)))
			.retrieve()
			.bodyToMono(String.class)
			.block();
		return objectMapper.readValue(result, PostDepositTransferResponseDto.class);
	}

	/**
	 *  finAccount로 잔액 조회
	 *
	 * @param finAccount String
	 * @return GetInquireBalanceResponseDto
	 */
	public GetInquireBalanceResponseDto getAccountAmount(String finAccount) throws JsonProcessingException {
		String result = WebClient.create()
			.post()
			.uri(nhApiConstantProvider.getUrl() + "/InquireBalance.nh")
			.header("Content-type", "application/json;charset=utf-8")
			.bodyValue(
				objectMapper.writeValueAsString(GetInquireBalanceRequestDto.of(nhApiConstantProvider,
					nhApiManagementService.updateIsTuno(), finAccount)))
			.retrieve()
			.bodyToMono(String.class)
			.block();
		return objectMapper.readValue(result, GetInquireBalanceResponseDto.class);
	}
}
