package com.pangpang.airbank.domain.savings.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.savings.domain.Savings;
import com.pangpang.airbank.global.meta.domain.SavingsStatus;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {

	Optional<Savings> findByGroupIdAndStatusEquals(Long groupId, SavingsStatus status);

	Boolean existsByStatusEquals(SavingsStatus status);

	Boolean existsByGroupIdAndStatusEquals(Long id, SavingsStatus status);

	Optional<Savings> findByIdAndStatusEquals(Long id, SavingsStatus savingsStatus);

	@Query("select s from savings s "
		+ "join fetch s.group g "
		+ "join fetch g.parent "
		+ "join fetch g.child "
		+ "where g.id = :id and s.status = :status")
	Optional<Savings> findByIdAndStatusEqualsWithGroupAndParentAndChild(Long id, @Param("status") SavingsStatus status);

	@Query("select s from savings s "
		+ "join fetch s.group g "
		+ "join fetch g.child "
		+ "where s.status = :status")
	List<Savings> findAllByStatusEqualsWithGroupAndChild(@Param("status") SavingsStatus status);
}
