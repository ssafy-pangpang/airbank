package com.pangpang.airbank.domain.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.member.domain.CreditHistory;
import com.pangpang.airbank.domain.member.domain.Member;

@Repository
public interface CreditHistoryRepository extends JpaRepository<CreditHistory, Long> {

	List<CreditHistory> findAllByMemberId(Long memberId);
}
