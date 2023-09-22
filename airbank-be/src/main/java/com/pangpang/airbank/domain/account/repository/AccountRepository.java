package com.pangpang.airbank.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.account.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
