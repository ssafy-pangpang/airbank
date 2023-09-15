package com.pangpang.airbank.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	@Query("select m from member m "
		+ "where m.phoneNumber = :phoneNumber and m.role = 'CHILD'")
	Optional<Member> findByChildPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
