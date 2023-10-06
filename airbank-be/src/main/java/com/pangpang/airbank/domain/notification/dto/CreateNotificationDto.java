package com.pangpang.airbank.domain.notification.dto;

import com.pangpang.airbank.domain.account.dto.TransferRequestDto;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.savings.domain.SavingsItem;
import com.pangpang.airbank.global.meta.domain.NotificationType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateNotificationDto {
	private String content;
	private Long senderId;
	private Long receiverId;
	private Long groupId;
	private Long savingsId;
	private NotificationType notificationType;

	public static CreateNotificationDto from(TransferRequestDto transferRequestDto, String type) {
		Member receiver = null, sender = null;
		String msgType = "";

		if (type.equals("withdraw")) {
			receiver = transferRequestDto.getSenderAccount().getMember();
			sender = transferRequestDto.getReceiverAccount().getMember();
			msgType = "출금했습니다";
		} else if (type.equals("deposit")) {
			receiver = transferRequestDto.getReceiverAccount().getMember();
			sender = transferRequestDto.getSenderAccount().getMember();
			msgType = "입금받았습니다";
		}

		return CreateNotificationDto.builder()
			.content(String.format("%s님에게 %s %s원을 %s.", sender.getName(), transferRequestDto.getType().getMsgName(),
				transferRequestDto.getAmount().toString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ","), msgType))
			.senderId(sender.getId())
			.receiverId(receiver.getId())
			.notificationType(NotificationType.ofName(transferRequestDto.getType().getName()))
			.build();
	}

	public static CreateNotificationDto of(String content, Member receiver, NotificationType notificationType) {
		return CreateNotificationDto.builder()
			.content(content)
			.senderId(null)
			.receiverId(receiver.getId())
			.notificationType(notificationType)
			.build();
	}

	public static CreateNotificationDto ofGroupConfirm(Member child, Member parent, Long groupId) {
		return CreateNotificationDto.builder()
			.content(String.format("%s님에게 자녀 등록을 요청받았습니다.", parent.getName()))
			.senderId(parent.getId())
			.receiverId(child.getId())
			.groupId(groupId)
			.notificationType(NotificationType.GROUP_CONFIRM)
			.build();
	}

	public static CreateNotificationDto ofGroup(Member child, Member parent, Boolean isAccept) {
		return CreateNotificationDto.builder()
			.content(String.format("%s님이 자녀 등록을 %s하였습니다.", child.getName(), isAccept ? "수락" : "거절"))
			.senderId(child.getId())
			.receiverId(parent.getId())
			.notificationType(NotificationType.GROUP)
			.build();
	}

	public static CreateNotificationDto ofSavingsConfirm(Member child, Member parent, Long groupId,
		SavingsItem savingsItem) {
		return CreateNotificationDto.builder()
			.content(String.format("%s님에게 %s %s원 상품의 티끌 모으기를 요청 받았습니다.", child.getName(), savingsItem.getName(),
				savingsItem.getAmount().toString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",")))
			.senderId(child.getId())
			.receiverId(parent.getId())
			.groupId(groupId)
			.notificationType(NotificationType.SAVINGS_CONFIRM)
			.build();
	}

	public static CreateNotificationDto ofSavings(Member child, Member parent, SavingsItem savingsItem,
		Boolean isAccept) {
		return CreateNotificationDto.builder()
			.content(String.format("%s님이 %s %s원 상품의 티끌 모으기를 %s 하였습니다.", parent.getName(), savingsItem.getName(),
				savingsItem.getAmount().toString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ","), isAccept ? "수락" : "거절"))
			.senderId(parent.getId())
			.receiverId(child.getId())
			.notificationType(NotificationType.SAVINGS)
			.build();
	}

	public static CreateNotificationDto ofSavingsRewardConfirm(Member child, Member parent, SavingsItem savingsItem) {
		return CreateNotificationDto.builder()
			.content(
				String.format("%s님이 %s %s원 상품의 티끌 모으기를 완료 하였습니다.", child.getName(), savingsItem.getName(),
					savingsItem.getAmount().toString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",")))
			.senderId(child.getId())
			.receiverId(parent.getId())
			.notificationType(NotificationType.SAVINGS_REWARD_CONFIRM)
			.build();
	}
}
