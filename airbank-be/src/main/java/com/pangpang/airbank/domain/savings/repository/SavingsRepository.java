package com.pangpang.airbank.domain.savings.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.savings.domain.Savings;
import com.pangpang.airbank.global.meta.domain.SavingsStatus;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {

	Optional<Savings> findByGroupIdAndStatusEquals(Long groupId, SavingsStatus status);

	Boolean existsByStatusEquals(SavingsStatus status);

	Boolean existsByGroupIdAndStatusEquals(Long id, SavingsStatus status);
}
