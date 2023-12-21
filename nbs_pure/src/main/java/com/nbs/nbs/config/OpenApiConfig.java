package com.nbs.nbs.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


@OpenAPIDefinition(
	info = @Info(
		contact = @Contact(
			name = "tsm_dev",
			email = "harimc@temtech.co.kr"
		),
		description = "OpenApi documentation for nbs project",
		title = "OpenApi specification - tsmtech",
		version = "1.0",
		termsOfService = "Terms of service"
	),
	servers = {
		@Server(
			url = "http://222.99.194.215:8080",
			description = "TestServer ENV"
		),
		@Server(
				url = "http://localhost:8080",
				description = "dev Local ENV"
		),
	},
	security = {
		@SecurityRequirement(
			name = "bearerAuth"
		)
	}
)
@SecurityScheme(
	name = "bearerAuth",
	description = "JWT auth description",
	scheme = "bearer",
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	in = SecuritySchemeIn.HEADER
)
@SecurityScheme(
	name = "basicAuth", // Basic 인증 추가
	description = "Basic auth description",
	scheme = "basic",
	type = SecuritySchemeType.HTTP,
	in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public OperationCustomizer addGlobalHeaderCustomizer() {
		return (operation, handlerMethod) -> {
			HeaderParameter menuHeader = (HeaderParameter) new HeaderParameter()
					.name("menu")
					.description("menu Id를 입력하면 메뉴에 대한 접근권한을 확인합니다. Admin 기준 NBSZ0000 테스트 용으로 사용가능,")
					.required(false)
					.example("NBSZ0000");

			operation.addParametersItem(menuHeader);
			return operation;
		};
	}
}
