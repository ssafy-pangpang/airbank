package com.pangpang.airbank.domain.savings.domain;

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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "savings_item")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingsItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 255)
	@NotNull
	@Column
	private String name;

	@NotNull
	@Column
	private Long amount;

	@Size(max = 255)
	@Column
	private String imageUrl;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "savings_id", foreignKey = @ForeignKey(name = "fk_savings_item_to_savings_savings_id"))
	private Savings savings;
}
