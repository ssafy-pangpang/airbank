package com.pangpang.airbank.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pangpang.airbank.domain.account.domain.AccountHistory;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {
}
