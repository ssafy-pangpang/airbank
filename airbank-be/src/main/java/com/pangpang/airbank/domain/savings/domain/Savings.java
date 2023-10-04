package com.pangpang.airbank.domain.savings.domain;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.BaseTimeEntity;
import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.savings.dto.PatchConfirmSavingsRequestDto;
import com.pangpang.airbank.domain.savings.dto.PostSaveSavingsRequestDto;
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
	@Builder.Default
	@ColumnDefault("0")
	@Column
	private Long totalAmount = 0L;

	@Column
	private LocalDate startedAt;

	@Column
	private LocalDate expiredAt;

	@Column
	private LocalDate endedAt;

	@NotNull
	@Column
	private Integer month;

	@NotNull
	@Builder.Default
	@ColumnDefault("0")
	@Column
	private Integer paymentCount = 0;

	@NotNull
	@Builder.Default
	@ColumnDefault("0")
	@Column
	private Integer delayCount = 0;

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

	public static Savings of(Group group, PostSaveSavingsRequestDto postSaveSavingsRequestDto) {
		Long amount = postSaveSavingsRequestDto.getAmount();
		Integer month = postSaveSavingsRequestDto.getMonth();
		Long parentsAmount = postSaveSavingsRequestDto.getParentsAmount();

		return Savings.builder()
			.myAmount(amount - parentsAmount)
			.parentsAmount(parentsAmount)
			.monthlyAmount((amount - parentsAmount) / month)
			.startedAt(LocalDate.now())
			.expiredAt(LocalDate.now().plusMonths(month))
			.month(month)
			.group(group)
			.build();
	}

	public void confirmSavings(PatchConfirmSavingsRequestDto patchConfirmSavingsRequestDto) {
		if (patchConfirmSavingsRequestDto.getIsAccept()) {
			LocalDate now = LocalDate.now();

			this.status = SavingsStatus.PROCEEDING;
			this.startedAt = now;
			this.expiredAt = now.plusMonths(this.month);
			return;
		}
		this.status = SavingsStatus.REJECT;
	}

	public Long getAmountThisMonth() {
		if (this.getMonth() - this.getPaymentCount() == 1) {
			return this.getMyAmount() - this.getTotalAmount();
		}
		return this.getMonthlyAmount();
	}

	public void updateStatus(SavingsStatus status) {
		this.status = status;
	}

	public Boolean isPaidThisMonth() {
		LocalDate startDate = this.startedAt.plusMonths(this.paymentCount + this.delayCount);
		LocalDate endDate = startDate.plusMonths(1);
		LocalDate now = LocalDate.now();
		return (!startDate.isBefore(now) && !startDate.equals(now)) || (!endDate.isAfter(now) && !endDate.equals(now));
	}

	public void transferSavings(Long amount) {
		if (this.getMonth() - this.getPaymentCount() == 1) {
			this.endedAt = LocalDate.now();
		}
		this.totalAmount += amount;
		this.paymentCount++;
	}

	public void delay() {
		this.delayCount++;
		this.expiredAt = this.expiredAt.plusMonths(1);
	}
}