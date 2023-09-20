package com.pangpang.airbank.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangpang.airbank.domain.group.domain.MemberRelationship;
import com.pangpang.airbank.domain.group.repository.MemberRelationshipRepository;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.MemberRepository;
import com.pangpang.airbank.domain.notification.domain.Notification;
import com.pangpang.airbank.domain.notification.dto.CreateNotificationDto;
import com.pangpang.airbank.domain.notification.dto.GetNotificationRequestDto;
import com.pangpang.airbank.domain.notification.dto.NotificationElement;
import com.pangpang.airbank.domain.notification.repository.NotificationRepository;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.exception.MemberException;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.error.info.MemberErrorInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final MemberRelationshipRepository memberRelationshipRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	@Override
	public GetNotificationRequestDto inquireNotification(Long memberId, Long groupId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorInfo.NOT_FOUND_MEMBER));

		MemberRelationship memberRelationship = memberRelationshipRepository.findById(groupId)
			.orElseThrow(() -> new GroupException(GroupErrorInfo.NOT_FOUND_MEMBER_RELATIONSHIP_BY_CHILD));

		Long partnerId = memberRelationship.getPartnerMember(member).getId();
		List<NotificationElement> notifications = notificationRepository.findByReceiverIdAndSenderId(memberId,
			partnerId);

		return GetNotificationRequestDto.from(notifications);
	}

	@Transactional
	@Override
	public void saveNotification(CreateNotificationDto createNotificationDto) {
		notificationRepository.save(Notification.from(createNotificationDto));
	}
}
