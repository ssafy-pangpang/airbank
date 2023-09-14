package com.pangpang.airbank.domain.group.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.group.domain.MemberRelationship;

@Repository
public interface MemberRelationshipRepository extends JpaRepository<MemberRelationship, Long> {
	@Query("select m from member_relationship m "
		+ "join fetch m.child c "
		+ "where m.parent.id = :parentId and m.activated = true ")
	List<MemberRelationship> findAllByParentIdWithChild(@Param("parentId") Long parentId);

	@Query("select m from member_relationship m "
		+ "join fetch m.parent c "
		+ "where m.child.id = :childId and m.activated = true ")
	List<MemberRelationship> findAllByChildIdWithParent(@Param("childId") Long childId);

}
