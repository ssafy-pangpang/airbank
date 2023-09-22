package com.pangpang.airbank.global.common.api.nh;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pangpang.airbank.domain.account.dto.PostEnrollAccountRequestDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetFinAccountRequestDto;
import com.pangpang.airbank.global.common.api.nh.dto.GetFinAccountResponseDto;
import com.pangpang.airbank.global.common.api.nh.service.NhApiManagementService;

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
		return objectMapper.readValue(result, GetFinAccountResponseDto.class);
	}
}
