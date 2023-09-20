package com.pangpang.airbank.domain.notification.service;

import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.domain.notification.dto.GetNotificationRequestDto;

public interface NotificationService {

	GetNotificationRequestDto inquireNotification(Long memberId, Long groupId);

	void saveNotification(CreateNotificationDto createNotificationDto);
}
