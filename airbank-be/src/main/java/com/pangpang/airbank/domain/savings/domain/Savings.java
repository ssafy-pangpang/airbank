package com.pangpang.airbank.domain.savings.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.BaseTimeEntity;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.global.meta.converter.SavingsStatusConverter;
import com.pangpang.airbank.global.meta.domain.SavingsStatus;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "savings")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Savings extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column
	private Long myAmount;

	@NotNull
	@Column
	private Long parentsAmount;

	@NotNull
	@Column
	private Long monthlyAmount;

	@NotNull
	@Column
	private LocalDateTime startedAt;

	@NotNull
	@Column
	private LocalDateTime expiredAt;

	@Column
	private LocalDateTime endedAt;

	@NotNull
	@Column
	private Integer month;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id",
		foreignKey = @ForeignKey(name = "fk_savings_to_group_group_id"))
	private Group group;

	@NotNull
	@Builder.Default
	@ColumnDefault("'PENDING'")
	@Column(length = 20)
	@Convert(converter = SavingsStatusConverter.class)
	private SavingsStatus status = SavingsStatus.PENDING;
}