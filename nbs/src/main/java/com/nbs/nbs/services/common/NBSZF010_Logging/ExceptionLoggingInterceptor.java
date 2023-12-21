package com.nbs.nbs.services.common.NBSZF010_Logging;

import java.sql.SQLException;
import java.time.Instant;

import com.nbs.nbs.entity.logging.ErrorLoggingRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ExceptionLoggingInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggingInterceptor.class);
	private final ErrorLoggingRepository errorLoggingRepository;

	public ExceptionLoggingInterceptor(ErrorLoggingRepository errorLoggingRepository) {
		this.errorLoggingRepository = errorLoggingRepository;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {   // 요청이 들어온 후, 처리가 끝난 다음 실행하기 떄문에 void.
		if (ex != null) {

			String originalUrl = (String) request.getAttribute("originalUrl"); // 에러 발생시 redirecting 되기 전의 URL을 가져옴
			String menuURL = originalUrl != null ? originalUrl : request.getRequestURL().toString();
			String userId = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "GUEST";
			String statusCode = ""; // 초기 상태 코드 설정을 null로 변경
			String errorCode = ""; // 초기 에러 코드 설정을 null로 변경
			String errorName = ex.getClass().getSimpleName();
			String errorMsg = ex.getMessage();
			String createdBy = "SYSTEM";
			Instant createdAt = Instant.now();

			if (ex instanceof ResponseStatusException) {
				statusCode = ((ResponseStatusException) ex).getStatusCode().toString(); // ResponseStatusException 경우 상태 코드를 가져옴.
			} else if (ex instanceof SQLException) {
				errorCode = "SQL"; // SQLException 경우 오류 코드를 'SQL'로 설정
				// errorMsg = extractSqlQueryFromException(ex); // SQL 예외에서 쿼리 추출 - MyBatis 로깅 설정 필요
			}

			logger.error("menu_url : {}, user_id : {}, status_code : {}, error_code : {}, error_name : {}, error_msg : {}, created_by : {}, createdAt : {}",
					menuURL, userId, statusCode, errorCode, errorName, errorMsg, createdBy, createdAt.toString());

			ErrorLoggingService.errorlogToDB(menuURL, userId, statusCode, errorCode, errorName, errorMsg, createdBy, createdAt, errorLoggingRepository);
		}
	}

}
