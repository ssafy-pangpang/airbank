package com.pangpang.airbank.domain.notification.domain;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.BaseTimeEntity;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.converter.NotificationTypeConverter;
import com.pangpang.airbank.global.meta.domain.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "notification")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 255)
	@NotNull
	@Column
	private String content;

	@NotNull
	@Builder.Default
	@ColumnDefault("false")
	@Column
	private Boolean activated = Boolean.FALSE;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", foreignKey = @ForeignKey(name = "fk_notification_to_member_sender_id"))
	private Member sender;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", foreignKey = @ForeignKey(name = "fk_notification_to_member_receiver_id"))
	private Member receiver;

	@NotNull
	@Column(length = 20)
	@Convert(converter = NotificationTypeConverter.class)
	private NotificationType notificationType;
}
