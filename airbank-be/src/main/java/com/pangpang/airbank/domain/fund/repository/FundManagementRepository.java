package com.pangpang.airbank.domain.fund.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pangpang.airbank.domain.fund.domain.FundManagement;

public interface FundManagementRepository extends JpaRepository<FundManagement, Long> {
	Optional<FundManagement> findByMemberRelationshipId(Long memberRelationshipId);

	Boolean existsByMemberRelationshipId(Long groupId);
}
