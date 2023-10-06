package com.pangpang.airbank.domain.notification.service;

import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.domain.notification.dto.GetNotificationResponseDto;

public interface NotificationService {

	GetNotificationResponseDto inquireNotification(Long memberId, Long groupId);

	void saveNotification(CreateNotificationDto createNotificationDto);
}
