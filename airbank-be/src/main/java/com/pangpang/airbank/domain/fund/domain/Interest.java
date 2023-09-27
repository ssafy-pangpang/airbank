package com.pangpang.airbank.domain.fund.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.BaseTimeEntity;
import com.pangpang.airbank.domain.group.domain.Group;

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

@Entity(name = "interest")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
