package com.pangpang.airbank.domain.account.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.account.domain.AccountHistory;
import com.pangpang.airbank.domain.account.dto.AccountHistoryElement;
import com.pangpang.airbank.domain.account.dto.SumAmountByAccount;
import com.pangpang.airbank.global.meta.domain.TransactionDistinction;
import com.pangpang.airbank.global.meta.domain.TransactionType;

@Repository
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

	// AccountHistoryElement로 조회
	@Query("select new com.pangpang.airbank.domain.account.dto.AccountHistoryElement(a.amount, a.apiCreatedAt, "
		+ "a.transactionType, a.transactionDistinction, a.transactionPartner.name) "
		+ "from account_history a "
		+ "where a.account = :account")
	List<AccountHistoryElement> findAllAccountHistoryByAccount(Account account);

	// 이번 달 세금 환급 여부
	@Query("""
		 	SELECT COUNT(ah) > 0 FROM account_history ah
			WHERE ah.transactionPartner.id=:childId AND ah.transactionType=:type
			AND YEAR(ah.apiCreatedAt)=YEAR(:expiredAt) AND MONTH(ah.apiCreatedAt)=MONTH(:expiredAt)
		""")
	Boolean existsAccountHistoryByApiCreatedAtAndGroupIdAndTransactionType(Long childId, LocalDate expiredAt,
		TransactionType type);
}
