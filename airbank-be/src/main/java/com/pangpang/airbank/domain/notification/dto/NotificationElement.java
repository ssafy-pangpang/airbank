package com.pangpang.airbank.domain.notification.dto;

import java.time.LocalDateTime;

import com.pangpang.airbank.domain.notification.domain.Notification;
import com.pangpang.airbank.global.meta.domain.NotificationType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationElement {
	private String content;
	private Boolean activated;
	private NotificationType notificationType;
	private LocalDateTime createdAt;

	public static NotificationElement from(Notification notification) {
		return NotificationElement.builder()
			.content(notification.getContent())
			.activated(notification.getActivated())
			.notificationType(notification.getNotificationType())
			.createdAt(notification.getCreatedAt())
			.build();

	}
}
