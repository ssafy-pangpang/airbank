package com.pangpang.airbank.domain.notification.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetNotificationResponseDto {
	private List<NotificationElement> notificationElements;

	public static GetNotificationResponseDto from(List<NotificationElement> notificationElements) {
		return GetNotificationResponseDto.builder()
			.notificationElements(notificationElements)
			.build();
	}
}
