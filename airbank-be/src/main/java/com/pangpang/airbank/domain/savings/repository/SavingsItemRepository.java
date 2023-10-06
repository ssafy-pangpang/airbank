package com.pangpang.airbank.domain.savings.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.savings.domain.Savings;
import com.pangpang.airbank.domain.savings.domain.SavingsItem;

@Repository
public interface SavingsItemRepository extends JpaRepository<SavingsItem, Long> {
	Optional<SavingsItem> findBySavingsId(Long savingsId);

	Optional<SavingsItem> findBySavings(Savings savings);
}
