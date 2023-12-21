package com.nbs.nbs.services.common.NBSZF010_Logging;

import java.time.Instant;

import com.nbs.nbs.entity.logging.ErrorLoggingRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class MenuDeniedLoggingInterceptor implements AccessDeniedHandler {

	private static final Logger logger = LoggerFactory.getLogger(MenuDeniedLoggingInterceptor.class);

	private final ErrorLoggingRepository errorLoggingRepository;

	@Autowired
	public MenuDeniedLoggingInterceptor(ErrorLoggingRepository errorLoggingRepository) {
		this.errorLoggingRepository = errorLoggingRepository;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.access.AccessDeniedException accessDeniedException)
			throws java.io.IOException, jakarta.servlet.ServletException {

		String originalUrl = (String) request.getAttribute("originalUrl");    // 에러발생시 redirecting 되기 전의 URL을 가져옴
		String menuURL = originalUrl != null ? originalUrl : request.getRequestURL().toString();
		String userId = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "GUEST";
		String statusCode = String.valueOf(response.getStatus());
		String errorCode = "";
		String errorName = accessDeniedException.getClass().getSimpleName();
		String errorMsg = accessDeniedException.getMessage();
		String createdBy = "SYSTEM";
		Instant createdAt = Instant.now();

		logger.error("menu_url : {}, user_id : {}, status_code : {}, error_code :{}, error_name : {}, error_msg : {}, created_by : {}, createdAt : {}",
				menuURL, userId, statusCode, errorCode, errorName, errorMsg, createdBy, createdAt.toString());

		ErrorLoggingService.errorlogToDB(menuURL, userId, statusCode, errorCode, errorName, errorMsg, createdBy, createdAt, errorLoggingRepository);

//		response.sendRedirect("/");
	}
}
