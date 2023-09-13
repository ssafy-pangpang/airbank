package com.pangpang.airbank.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	@GetMapping("/login")
	private ResponseEntity<Void> login(HttpServletRequest request) {
		/*
		TODO: 카카오 로직 추가
		 */
		HttpSession session = request.getSession();
		session.setAttribute("memberId", "1");

		return ResponseEntity.ok()
			.body(null);

	}
}
