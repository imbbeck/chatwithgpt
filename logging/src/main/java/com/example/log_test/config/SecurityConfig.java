package com.example.log_test.config;

import com.example.log_test.logging.interceptor.MenuDeniedLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final MenuDeniedLoggingInterceptor menuDeniedLoggingInterceptor;

	@Autowired
	public SecurityConfig(MenuDeniedLoggingInterceptor menuDeniedLoggingInterceptor) {
		this.menuDeniedLoggingInterceptor = menuDeniedLoggingInterceptor;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("user").password("1").roles("USER")
				.and()
				.withUser("admin").password("1").roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/user/**").hasRole("USER")
				.antMatchers("/").permitAll()
				.and()
				.formLogin()
				.and()
				.exceptionHandling()
				.accessDeniedHandler(menuDeniedLoggingInterceptor);
	}
}
