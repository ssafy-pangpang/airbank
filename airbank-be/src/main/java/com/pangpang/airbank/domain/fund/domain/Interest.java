package com.pangpang.airbank.domain.fund.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.BaseTimeEntity;
import com.pangpang.airbank.domain.group.domain.MemberRelationship;

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

	@NotNull
	@Column
	private LocalDateTime paidAt;

	@NotNull
	@Column
	private LocalDateTime billedAt;

	@NotNull
	@Column
	private LocalDateTime expiredAt;

	@NotNull
	@Builder.Default
	@ColumnDefault("false")
	@Column
	private Boolean activated = Boolean.FALSE;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_relationship_id",
		foreignKey = @ForeignKey(name = "fk_interest_to_member_relationship_member_relationship_id"))
	private MemberRelationship memberRelationship;
}
