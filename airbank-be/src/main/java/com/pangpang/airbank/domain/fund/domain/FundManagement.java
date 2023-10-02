package com.pangpang.airbank.domain.fund.domain;

import org.hibernate.annotations.ColumnDefault;

import com.pangpang.airbank.domain.group.domain.Group;
import com.pangpang.airbank.domain.group.dto.CommonFundManagementRequestDto;

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
	@ColumnDefault("10")
	@Column
	private Integer taxRate = 10;

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
	@JoinColumn(name = "group_id",
		foreignKey = @ForeignKey(name = "fk_fund_management_to_group_group_id"))
	private Group group;

	public void updateFundManagement(CommonFundManagementRequestDto commonFundManagementRequestDto) {
		this.taxRate = commonFundManagementRequestDto.getTaxRate();
		this.allowanceAmount = commonFundManagementRequestDto.getAllowanceAmount();
		this.allowanceDate = commonFundManagementRequestDto.getAllowanceDate();
		this.confiscationRate = commonFundManagementRequestDto.getConfiscationRate();
		this.loanLimit = commonFundManagementRequestDto.getLoanLimit();
	}

	public void plusLoanAmount(Long loanAmount) {
		this.loanAmount += loanAmount;
	}

	public void resetLoanLimitAndLoanAmount(Long loanLimit, Long loanAmount) {
		this.loanLimit = loanLimit;
		this.loanAmount = loanAmount;
	}

	public void minusLoanAmount(Long loanAmount) {
		this.loanAmount -= loanAmount;
	}

	public static FundManagement of(Group group,
		CommonFundManagementRequestDto commonFundManagementRequestDto) {
		return FundManagement.builder()
			.taxRate(commonFundManagementRequestDto.getTaxRate())
			.allowanceAmount(commonFundManagementRequestDto.getAllowanceAmount())
			.allowanceDate(commonFundManagementRequestDto.getAllowanceDate())
			.confiscationRate(commonFundManagementRequestDto.getConfiscationRate())
			.loanLimit(commonFundManagementRequestDto.getLoanLimit())
			.group(group)
			.build();
	}
}
