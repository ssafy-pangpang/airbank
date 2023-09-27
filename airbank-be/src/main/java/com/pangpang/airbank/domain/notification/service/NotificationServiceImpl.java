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

	/**
	 *  알림들 조회
	 *
	 * @param memberId Long
	 * @param groupId Long
	 * @return GetNotificationResponseDto
	 * @see GroupRepository
	 * @see NotificationGroup
	 * @see NotificationElement
	 */
	@Transactional
	@Override
	public GetNotificationResponseDto inquireNotification(Long memberId, Long groupId) {
		Group group = groupRepository.findById(groupId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_CHILD));

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

	/**
	 * 알림 저장
	 *
	 * @param createNotificationDto
	 * @see NotificationGroup
	 */
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

	/**
	 * 알림 조회시, 알림 element의 리스트 생성
	 *
	 * @param notificationElements
	 * @param notification
	 */
	private void addNotificationElements(List<NotificationElement> notificationElements, Notification notification) {
		notificationElements.add(NotificationElement.from(notification));
	}

	/**
	 * 알림 조회시 읽음 처리
	 *
	 * @param notification
	 */
	private void activateNotificationActivated(Notification notification) {
		notification.activateActivated();
	}

	/**
	 * 몽고DB의 ID 값 생성
	 *
	 * @param senderId
	 * @param receiverId
	 * @return String
	 */
	private String makeNotificationGroupId(Long senderId, Long receiverId) {
		return senderId + ID_DELIMITER + receiverId;
	}
}
