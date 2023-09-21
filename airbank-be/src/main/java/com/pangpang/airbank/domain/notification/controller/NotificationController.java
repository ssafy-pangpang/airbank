package com.pangpang.airbank.domain.notification.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.domain.notification.dto.GetNotificationResponseDto;
import com.pangpang.airbank.domain.notification.service.NotificationService;
import com.pangpang.airbank.global.common.response.EnvelopeResponse;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping()
	public ResponseEntity<EnvelopeResponse<GetNotificationResponseDto>> inquireNotification(@RequestParam Long groupId
		// , @Authentication AuthenticatedMemberArgument authenticatedMemberArgument
	) {
		AuthenticatedMemberArgument authenticatedMemberArgument = new AuthenticatedMemberArgument(1L);

		return ResponseEntity.ok()
			.body(EnvelopeResponse.<GetNotificationResponseDto>builder()
				.code(HttpStatus.OK.value())
				.data(notificationService.inquireNotification(authenticatedMemberArgument.getMemberId(), groupId))
				.build());
	}

	@PostMapping()
	public void saveNotification(@RequestBody CreateNotificationDto createNotificationDto) {
		notificationService.saveNotification(createNotificationDto);
	}
}
