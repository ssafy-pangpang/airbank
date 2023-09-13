package com.pangpang.airbank.domain.member.domain;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.BaseTimeEntity;

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

@Entity(name = "credit_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditHistory extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Builder.Default
	@ColumnDefault("632")
	@Column
	private Integer creditScore = 632;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_credit_history_to_member_member_id"))
	private Member member;
}
