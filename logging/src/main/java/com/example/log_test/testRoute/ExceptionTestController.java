package com.example.log_test.testRoute;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionTestController {

	@GetMapping("/test-exception")
	public String testException() {
		throw new NullPointerException("테스트 예외 발생");
	}
}

