package com.pangpang.airbank.domain.notification.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.global.meta.converter.NotificationTypeConverter;
import com.pangpang.airbank.global.meta.domain.NotificationType;

import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "notification")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

	@NotNull
	private String content;

	@NotNull
	@Builder.Default
	private Boolean activated = Boolean.FALSE;

	@NotNull
	private Long senderId;

	@NotNull
	private Long receiverId;

	@NotNull
	@Convert(converter = NotificationTypeConverter.class)
	private NotificationType notificationType;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@NotNull
	@LastModifiedDate
	private LocalDateTime updatedAt;

	public static Notification from(CreateNotificationDto createNotificationDto) {
		return Notification.builder()
			.content(createNotificationDto.getContent())
			.senderId(createNotificationDto.getSenderId())
			.receiverId(createNotificationDto.getReceiverId())
			.notificationType(NotificationType.ofName(createNotificationDto.getNotificationType()))
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

	}
}
