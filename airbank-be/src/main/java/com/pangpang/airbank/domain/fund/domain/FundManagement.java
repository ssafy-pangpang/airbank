package com.pangpang.airbank.domain.fund.domain;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.group.domain.MemberRelationship;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "fund_management")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundManagement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Builder.Default
	@ColumnDefault("50")
	@Column
	private Integer taxRate = 50;

	@NotNull
	@Column
	private Long allowanceAmount;

	@NotNull
	@Column
	private Integer allowanceDate;

	@NotNull
	@Builder.Default
	@ColumnDefault("50")
	@Column
	private Integer confiscationRate = 50;

	@NotNull
	@Builder.Default
	@ColumnDefault("0")
	@Column
	private Long loanLimit = 0L;

	@NotNull
	@Builder.Default
	@ColumnDefault("0")
	@Column
	private Long loanAmount = 0L;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_relationship_id",
		foreignKey = @ForeignKey(name = "fk_fund_management_to_member_relationship_member_relationship_id"))
	private MemberRelationship memberRelationship;
}
