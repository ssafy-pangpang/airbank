package com.pangpang.airbank.domain.account.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.pangpang.airbank.domain.account.dto.SaveDepositHistoryRequestDto;
import com.pangpang.airbank.domain.account.dto.SaveWithdrawalHistoryRequestDto;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.converter.TransactionDistinctionConverter;
import com.pangpang.airbank.global.meta.converter.TransactionTypeConverter;
import com.pangpang.airbank.global.meta.domain.TransactionDistinction;
import com.pangpang.airbank.global.meta.domain.TransactionType;

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

@Entity(name = "account_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column
	private UUID transactionIdentifier;

	@NotNull
	@Column
	private Long amount;

	@NotNull
	@Column
	private LocalDateTime apiCreatedAt;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "fk_account_history_to_account_account_id"))
	private Account account;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transaction_partner_id",
		foreignKey = @ForeignKey(name = "fk_account_history_to_member_transaction_partner_id"))
	private Member transactionPartner;

	@NotNull
	@Column(length = 20)
	@Convert(converter = TransactionTypeConverter.class)
	private TransactionType transactionType;

	@NotNull
	@Column(length = 20)
	@Convert(converter = TransactionDistinctionConverter.class)
	private TransactionDistinction transactionDistinction;

	public static AccountHistory from(SaveWithdrawalHistoryRequestDto saveWithdrawalHistoryRequestDto) {
		return AccountHistory.builder()
			.transactionIdentifier(UUID.randomUUID())
			.amount(saveWithdrawalHistoryRequestDto.getAmount())
			.apiCreatedAt(LocalDateTime.now())
			.account(saveWithdrawalHistoryRequestDto.getAccount())
			.transactionPartner(saveWithdrawalHistoryRequestDto.getTransactionPartner())
			.transactionType(saveWithdrawalHistoryRequestDto.getTransactionType())
			.transactionDistinction(saveWithdrawalHistoryRequestDto.getTransactionDistinction())
			.build();
	}

	public static AccountHistory from(SaveDepositHistoryRequestDto saveDepositHistoryRequestDto) {
		return AccountHistory.builder()
			.transactionIdentifier(UUID.randomUUID())
			.amount(saveDepositHistoryRequestDto.getAmount())
			.apiCreatedAt(LocalDateTime.now())
			.account(saveDepositHistoryRequestDto.getAccount())
			.transactionPartner(saveDepositHistoryRequestDto.getTransactionPartner())
			.transactionType(saveDepositHistoryRequestDto.getTransactionType())
			.transactionDistinction(saveDepositHistoryRequestDto.getTransactionDistinction())
			.build();
	}
}
