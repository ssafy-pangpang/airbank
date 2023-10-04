package com.pangpang.airbank.domain.fund.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pangpang.airbank.domain.fund.domain.Tax;
import com.pangpang.airbank.domain.group.domain.Group;

public interface TaxRepository extends JpaRepository<Tax, Long> {
	// expiredAt 기준 밀린 세금
	@Query("SELECT sum(amount) FROM tax WHERE group.id=:groupId AND activated=false AND expiredAt<:expiredAt")
	Long findOverAmountsByGroupIdAndActivatedFalseAndExpiredAtLessThan(Long groupId, LocalDate expiredAt);

	// 이번 달 납부 안한 세금
	Optional<Tax> findByGroupIdAndActivatedFalseAndExpiredAtGreaterThanEqual(Long groupId, LocalDate expiredAt);

	List<Tax> findAllByGroupAndActivatedFalse(Group group);

	// 저번 달 납부 안한 세금
	@Query("""
		 	SELECT t FROM tax t
			WHERE t.group.id=:groupId
			AND YEAR(t.expiredAt)=YEAR(:expiredAt) AND MONTH(t.expiredAt)=MONTH(:expiredAt)
		""")
	Optional<Tax> findByGroupIdAndExpiredAt_MonthValueAndExpiredAt_Year(Long groupId, LocalDate expiredAt);
}
