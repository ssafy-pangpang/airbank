package com.pangpang.airbank.domain.notification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.group.repository.GroupRepository;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.domain.notification.domain.Notification;
import com.pangpang.airbank.domain.notification.domain.NotificationGroup;
import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.domain.notification.dto.GetNotificationResponseDto;
import com.pangpang.airbank.domain.notification.dto.NotificationElement;
import com.pangpang.airbank.domain.notification.repository.NotificationGroupRepository;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private static final String ID_DELIMITER = "_";

	private final NotificationGroupRepository notificationGroupRepository;
	private final GroupRepository groupRepository;
	private final MemberRepository memberRepository;

	@Transactional
	@Override
	public GetNotificationResponseDto inquireNotification(Long memberId, Long groupId) {
		Group group = groupRepository.findById(groupId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_CHILD_ID));

		Long partnerId = group.getPartnerMember(memberId).getId();
		NotificationGroup notificationGroup = notificationGroupRepository.findById(
				makeNotificationGroupId(partnerId, memberId))
			.orElse(notificationGroupRepository.save(NotificationGroup.of(partnerId, memberId)));

		List<NotificationElement> notificationElements = new ArrayList<>();
		for (Notification notification : notificationGroup.getNotifications()) {
			addNotificationElements(notificationElements, notification);
			activateNotificationActivated(notification);
		}

		notificationGroupRepository.save(notificationGroup);

		return GetNotificationResponseDto.from(notificationElements);
	}

	@Transactional
	@Override
	public void saveNotification(CreateNotificationDto createNotificationDto) {
		Long senderId = createNotificationDto.getSenderId();
		Long receiverId = createNotificationDto.getReceiverId();

		String notificationGroupId = makeNotificationGroupId(senderId, receiverId);

		NotificationGroup notificationGroup = notificationGroupRepository.findById(notificationGroupId)
			.orElse(
				notificationGroupRepository.save(NotificationGroup.of(senderId, receiverId))
			);

		notificationGroup.saveNotification(Notification.from(createNotificationDto));

		notificationGroupRepository.save(notificationGroup);
	}

	private void addNotificationElements(List<NotificationElement> notificationElements, Notification notification) {
		notificationElements.add(NotificationElement.from(notification));
	}

	private void activateNotificationActivated(Notification notification) {
		notification.activateActivated();
	}

	private String makeNotificationGroupId(Long senderId, Long receiverId) {
		return senderId + ID_DELIMITER + receiverId;
	}
}
