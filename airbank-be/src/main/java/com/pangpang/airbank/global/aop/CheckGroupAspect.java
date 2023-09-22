package com.pangpang.airbank.global.aop;

import java.util.Arrays;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.pangpang.airbank.domain.group.service.GroupService;
import com.pangpang.airbank.global.error.exception.GroupException;
import com.pangpang.airbank.global.error.info.GroupErrorInfo;
import com.pangpang.airbank.global.resolver.dto.AuthenticatedMemberArgument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CheckGroupAspect {

	private final GroupService groupService;

	@Before("@annotation(CheckGroup) && (args(.., groupId) || args(groupId, ..))")
	public void checkGroup(JoinPoint joinPoint, Long groupId) {
		AuthenticatedMemberArgument member = findAuthenticatedMemberArgument(joinPoint);

		if (!groupService.isMemberInGroup(member.getMemberId(), groupId)) {
			throw new GroupException(GroupErrorInfo.NOT_FOUND_GROUP_BY_ID_AND_MEMBER_ID);
		}
	}

	private AuthenticatedMemberArgument findAuthenticatedMemberArgument(JoinPoint joinPoint) {
		Optional<AuthenticatedMemberArgument> authenticatedMemberArgument = Arrays.stream(joinPoint.getArgs())
			.filter(arg -> arg instanceof AuthenticatedMemberArgument)
			.map(arg -> (AuthenticatedMemberArgument)arg)
			.findFirst();

		return authenticatedMemberArgument.orElseThrow(
			() -> new IllegalArgumentException("AuthenticatedMemberArgument 파라미터를 찾을 수 없습니다."));
	}
}
