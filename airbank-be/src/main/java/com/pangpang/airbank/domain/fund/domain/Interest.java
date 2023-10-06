package com.pangpang.airbank.domain.fund.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.BaseTimeEntity;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.global.meta.domain.InterestRate;

import jakarta.persistence.Column;
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
import lombok.extern.slf4j.Slf4j;

@Entity(name = "interest")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Interest extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column
	private Long amount;

	@Column
	private LocalDateTime paidAt;

	@NotNull
	@Column
	private LocalDate billedAt;

	@NotNull
	@Column
	private LocalDate expiredAt;

	@NotNull
	@Builder.Default
	@ColumnDefault("false")
	@Column
	private Boolean activated = Boolean.FALSE;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id",
		foreignKey = @ForeignKey(name = "fk_interest_to_group_group_id"))
	private Group group;

	/**
	 *  activated 갱신
	 *
	 * @param status Boolean
	 */
	public void updateActivated(Boolean status) {
		this.activated = status;
	}

	public void updateAmount(Long amount, InterestRate interestRate) {
		Integer rate = interestRate.getInterestRate();
		this.amount = (long)Math.floor(amount * (rate / 100.0));
	}

	public static Interest of(Group group) {
		return Interest.builder()
			.amount(0L)
			.billedAt(LocalDate.now().plusMonths(1))
			.expiredAt(LocalDate.now().plusMonths(1).plusWeeks(1))
			.group(group)
			.build();
	}
}
