package com.pangpang.airbank.domain.fund.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pangpang.airbank.domain.fund.domain.Tax;

public interface TaxRepository extends JpaRepository<Tax, Long> {
	// expiredAt 기준 밀린 세금
	@Query("SELECT sum(amount) FROM tax WHERE group.id=:groupId AND activated=false AND expiredAt<:expiredAt")
	Long findOverAmountsByGroupIdAndActivatedFalseAndExpiredAtLessThan(Long groupId, LocalDate expiredAt);

	// 이번 달 세금
	Optional<Tax> findByGroupIdAndActivatedFalseAndExpiredAtGreaterThanEqual(Long groupId, LocalDate expiredAt);
}
