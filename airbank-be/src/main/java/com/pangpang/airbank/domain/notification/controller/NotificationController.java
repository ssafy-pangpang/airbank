package com.pangpang.airbank.domain.notification.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.notification.dto.GetNotificationResponseDto;
import com.pangpang.airbank.domain.notification.service.NotificationService;
import com.pangpang.airbank.global.aop.CheckGroup;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.Authentication;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 알림 관리하는 controller
 */
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name = "notifications", description = "알림 관리하는 API")
public class NotificationController {

	private final NotificationService notificationService;

	@Operation(summary = "알림 조회", description = "사용자의 알림을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "알림 조회 성공",
			content = @Content(schema = @Schema(implementation = GetNotificationResponseDto.class))),
		@ApiResponse(responseCode = "1303", description = "등록중인 그룹이 없습니다.", content = @Content)
	})
	@CheckGroup
	@GetMapping()
	public ResponseEntity<EnvelopeResponse<GetNotificationResponseDto>> inquireNotification(
		@Authentication AuthenticatedMemberArgument authenticatedMemberArgument, @RequestParam("group_id") Long groupId
	) {

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetNotificationResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(notificationService.inquireNotification(authenticatedMemberArgument.getMemberId(), groupId))
				.build());
	}
}
