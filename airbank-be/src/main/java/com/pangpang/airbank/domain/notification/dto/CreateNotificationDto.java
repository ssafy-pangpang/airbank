package com.pangpang.airbank.domain.notification.dto;

import lombok.Getter;

@Getter
public class CreateNotificationDto {
	private String content;
	private Long senderId;
	private Long receiverId;
	private String notificationType;
}
