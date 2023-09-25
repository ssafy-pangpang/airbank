package com.pangpang.airbank.domain.fund.domain;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;

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

@Entity(name = "tax")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tax {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column
	private Long amount;

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
		foreignKey = @ForeignKey(name = "fk_tax_to_group_group_id"))
	private Group group;
}
