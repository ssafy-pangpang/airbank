package com.pangpang.airbank.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
