package com.pangpang.airbank.domain.notification.dto;

import com.pangpang.airbank.global.meta.domain.NotificationType;

import lombok.Getter;

@Getter
public class CreateNotificationDto {
	private String content;
	private Long senderId;
	private Long receiverId;
	private NotificationType notificationType;
}
