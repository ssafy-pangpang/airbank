package com.pangpang.airbank.domain.member.service;

import org.springframework.stereotype.Service;

import com.pangpang.airbank.domain.member.domain.CreditHistory;
import com.pangpang.airbank.domain.member.domain.Member;
import com.pangpang.airbank.domain.member.repository.CreditHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditHistoryServiceImpl implements CreditHistoryService {
	private final CreditHistoryRepository creditHistoryRepository;

	/**
	 *  신용점수 저장
	 *
	 * @param Member member
	 * @return void
	 */
	@Override
	public void saveCreditHistory(Member member) {
		CreditHistory creditHistory = new CreditHistory(member);
		creditHistoryRepository.save(creditHistory);
	}
}
