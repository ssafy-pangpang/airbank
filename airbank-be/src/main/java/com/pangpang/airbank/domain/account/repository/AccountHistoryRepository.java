package com.pangpang.airbank.domain.account.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pangpang.airbank.domain.account.domain.AccountHistory;
import com.pangpang.airbank.domain.account.dto.SumAmountByAccount;
import com.pangpang.airbank.global.meta.domain.TransactionDistinction;
import com.pangpang.airbank.global.meta.domain.TransactionType;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {
	// 이번 달 Account 별 송금/출금 금액 합계
	@Query("""
		SELECT sum(h.amount) as sumAmount, account, h.account.member.id as memberId FROM account_history h
		WHERE h.transactionType IN (:transactionType) AND h.transactionDistinction IN (:transactionDistinction)
		AND YEAR(h.apiCreatedAt)=YEAR(:apiCreatedAt) AND MONTH(h.apiCreatedAt)=MONTH(:apiCreatedAt)
		GROUP BY h.account""")
	List<SumAmountByAccount> findAmountSumByAccountAndApiCreatedAt(@Param("apiCreatedAt") LocalDate apiCreatedAt,
		@Param("transactionType") TransactionType[] transactionType,
		@Param("transactionDistinction") TransactionDistinction[] transactionDistinction);
}
