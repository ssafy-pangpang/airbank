package com.pangpang.airbank.domain.notification.dto;

import java.util.List;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetNotificationRequestDto {
	private List<NotificationElement> notifications;

	public static GetNotificationRequestDto from(List<NotificationElement> notifications) {
		return GetNotificationRequestDto.builder()
			.notifications(notifications)
			.build();
	}
}
