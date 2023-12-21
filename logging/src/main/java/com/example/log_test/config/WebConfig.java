package com.example.log_test.config;


import com.example.log_test.logging.interceptor.ExceptionLoggingInterceptor;
import com.example.log_test.logging.interceptor.MenuLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private MenuLoggingInterceptor loggingInterceptor;

	@Autowired
	private ExceptionLoggingInterceptor exceptionLoggingInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggingInterceptor);
		registry.addInterceptor(exceptionLoggingInterceptor);
	}
}
