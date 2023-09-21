package com.pangpang.airbank.domain.notification.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.global.meta.converter.NotificationTypeConverter;
import com.pangpang.airbank.global.meta.domain.NotificationType;

import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "notification_group")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationGroup {

	@Id
	private String id;

	@Field
	@Builder.Default
	private List<Notification> notifications = new ArrayList<>();

	public static NotificationGroup of(Long senderId, Long receiverId) {
		return NotificationGroup.builder()
			.id(String.valueOf(senderId) + "_" + String.valueOf(receiverId))
			.build();
	}

	public void saveNotification(Notification notification) {
		this.notifications.add(notification);
	}

}
