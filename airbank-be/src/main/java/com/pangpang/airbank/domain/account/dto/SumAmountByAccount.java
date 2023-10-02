package com.pangpang.airbank.domain.account.dto;

import com.pangpang.airbank.domain.account.domain.Account;

/**
 * Account History에서 Account 별 amount 합을 구할 때 사용하는 Projection
 */
public interface SumAmountByAccount {
	Long getSumAmount();

	Account getAccount();

	Long getMemberId();
}
