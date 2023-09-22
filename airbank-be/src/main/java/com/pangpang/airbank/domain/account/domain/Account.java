package com.pangpang.airbank.domain.account.domain;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.pangpang.airbank.domain.BaseTimeEntity;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.converter.AccountTypeConverter;
import com.pangpang.airbank.global.meta.converter.BankCodeConverter;
import com.pangpang.airbank.global.meta.domain.AccountType;
import com.pangpang.airbank.global.meta.domain.BankCode;

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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "account")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Account extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 30)
	@NotNull
	@Column
	private String accountNumber;

	@Size(max = 40)
	@Column
	private String finAccountNumber;

	@NotNull
	@Builder.Default
	@ColumnDefault("false")
	@Column
	private Boolean deleted = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_account_to_member_member_id"))
	private Member member;

	@NotNull
	@Column(length = 20)
	@Convert(converter = BankCodeConverter.class)
	private BankCode bankCode;

	@Column(length = 20)
	@Convert(converter = AccountTypeConverter.class)
	private AccountType type;
}
