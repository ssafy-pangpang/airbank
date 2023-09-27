package com.pangpang.airbank.domain.fund.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pangpang.airbank.domain.fund.domain.Confiscation;

public interface ConfiscationRepository extends JpaRepository<Confiscation, Long> {
	Optional<Confiscation> findByGroupIdAndActivatedTrue(Long groupId);
}
