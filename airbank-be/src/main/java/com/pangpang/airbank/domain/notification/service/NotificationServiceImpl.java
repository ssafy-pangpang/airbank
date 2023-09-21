package com.pangpang.airbank.domain.notification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.group.domain.MemberRelationship;
import com.pangpang.airbank.domain.group.repository.MemberRelationshipRepository;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.domain.notification.domain.Notification;
import com.pangpang.airbank.domain.notification.domain.NotificationGroup;
import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.domain.notification.dto.GetNotificationResponseDto;
import com.pangpang.airbank.domain.notification.dto.NotificationElement;
import com.pangpang.airbank.domain.notification.repository.NotificationGroupRepository;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private static final String ID_DELIMITER = "_";

	private final NotificationGroupRepository notificationGroupRepository;
	private final MemberRelationshipRepository memberRelationshipRepository;
	private final MemberRepository memberRepository;

	@Transactional
	@Override
	public GetNotificationResponseDto inquireNotification(Long memberId, Long groupId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		MemberRelationship memberRelationship = memberRelationshipRepository.findById(groupId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_MEMBER_RELATIONSHIP_BY_CHILD));

		Long partnerId = memberRelationship.getPartnerMember(member).getId();
		String notificationGroupId = partnerId + ID_DELIMITER + memberId;
		NotificationGroup notificationGroup = notificationGroupRepository.findById(notificationGroupId)
			.orElse(notificationGroupRepository.save(NotificationGroup.of(partnerId, memberId)));

		List<NotificationElement> notificationElements = new ArrayList<>();
		for (Notification notification : notificationGroup.getNotifications()) {
			notificationElements.add(NotificationElement.from(notification));

			if (notification.getActivated() == Boolean.FALSE) {
				notification.activateActivated();
			}
		}

		notificationGroupRepository.save(notificationGroup);

		return GetNotificationResponseDto.from(notificationElements);
	}

	@Transactional
	@Override
	public void saveNotification(CreateNotificationDto createNotificationDto) {
		Long senderId = createNotificationDto.getSenderId();
		Long receiverId = createNotificationDto.getReceiverId();

		String notificationGroupId = senderId + ID_DELIMITER + receiverId;

		NotificationGroup notificationGroup = notificationGroupRepository.findById(notificationGroupId)
			.orElse(
				notificationGroupRepository.save(NotificationGroup.of(senderId, receiverId))
			);

		notificationGroup.saveNotification(Notification.from(createNotificationDto));

		notificationGroupRepository.save(notificationGroup);
	}
}
