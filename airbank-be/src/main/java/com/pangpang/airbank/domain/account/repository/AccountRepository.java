package com.pangpang.airbank.domain.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.account.domain.Account;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.global.meta.domain.AccountType;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findByAccountNumber(String accountNumber);

	Optional<Account> findByMemberIdAndType(Long memberId, AccountType type);

	Optional<Account> findFirstByMemberIsNullAndType(AccountType type);

	Optional<Account> findByMemberAndType(Member member, AccountType type);

	@Query("""
			select a from account a
			where (a.member.id=:memberId and a.type=:type) or (a.member is null and a.type=:type)
			order by a.id
			limit 1
		""")
	Optional<Account> findFirstByMemberIsNullAndTypeOrMemberIdAndType(Long memberId, AccountType type);
}
