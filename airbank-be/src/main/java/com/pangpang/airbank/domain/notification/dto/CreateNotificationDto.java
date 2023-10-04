package com.pangpang.airbank.domain.notification.dto;

import com.pangpang.airbank.domain.account.dto.TransferRequestDto;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.domain.NotificationType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateNotificationDto {
	private String content;
	private Long senderId;
	private Long receiverId;
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
}
