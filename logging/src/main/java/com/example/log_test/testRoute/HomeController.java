package com.example.log_test.testRoute;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/")
	public String home() {
		return "홈페이지";
	}

	@GetMapping("/user")
	public String user() {
		return "사용자 페이지";
	}

	@GetMapping("/admin")
	public String admin() {
		return "관리자 페이지";
	}
}