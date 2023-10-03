package com.pangpang.airbank.domain.fund.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pangpang.airbank.domain.fund.domain.FundManagement;
import com.pangpang.airbank.domain.group.domain.Group;

public interface FundManagementRepository extends JpaRepository<FundManagement, Long> {
	Optional<FundManagement> findByGroupId(Long groupId);

	Optional<FundManagement> findByGroup(Group group);

	Boolean existsByGroupId(Long groupId);

	@Query("select f from fund_management f "
		+ "join fetch f.group g "
		+ "join fetch g.child "
		+ "join fetch g.parent ")
	List<FundManagement> findAllWithGroup();
}
