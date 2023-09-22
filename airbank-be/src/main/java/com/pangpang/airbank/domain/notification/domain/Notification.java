package com.pangpang.airbank.domain.notification.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.global.meta.converter.NotificationTypeConverter;
import com.pangpang.airbank.global.meta.domain.NotificationType;

import jakarta.persistence.Convert;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Notification {
	private String content;

	@Builder.Default
	private Boolean activated = Boolean.FALSE;

	private Long senderId;

	private Long receiverId;

	@Convert(converter = NotificationTypeConverter.class)
	private NotificationType notificationType;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	public static Notification from(CreateNotificationDto createNotificationDto) {
		return Notification.builder()
			.content(createNotificationDto.getContent())
			.senderId(createNotificationDto.getSenderId())
			.receiverId(createNotificationDto.getReceiverId())
			.notificationType(createNotificationDto.getNotificationType())
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	public void activateActivated() {
		this.activated = true;
	}
}
