// 프리로딩 문제로 인해, 로그가 두번 찍히는 문제가 있음.

package com.example.log_test.logging.interceptor;

import com.example.log_test.logging.model.MenuLoggingEntity;
import com.example.log_test.logging.repository.MenuLoggingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@Component
public class MenuLoggingInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(MenuLoggingInterceptor.class);

	private final MenuLoggingRepository menuLoggingRepository;

	public MenuLoggingInterceptor(MenuLoggingRepository menuLoggingRepository) {
		this.menuLoggingRepository = menuLoggingRepository;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {    // 요청이 들어온 후, 컨트롤러에 도달하기 전에 실행하기 떄문에 boolean을 반환해야 함.

		String menuURL = request.getRequestURL().toString();
		String userId = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "GUEST";
		String logContent = "";
		String createdBy = "SYSTEM";
		Instant createdAt = Instant.now();

		if (!menuURL.contains("favicon.ico")) {  // favicon.ico를 제외

			logger.info("menu_URL : {}, user_id : {}, log_content : {}, created_by : {}, created_at : {}",
					menuURL, userId, logContent, createdBy, createdAt.toString());

			MenuLoggingEntity menuLoggingEntity = new MenuLoggingEntity();
			menuLoggingEntity.setMenuId(menuURL);
			menuLoggingEntity.setUserId(userId);
			menuLoggingEntity.setLogContent(logContent);
			menuLoggingEntity.setCreatedBy(createdBy);
			menuLoggingEntity.setCreatedAt(createdAt);
			menuLoggingRepository.save(menuLoggingEntity);
		}

		return true;
	}
}

//		StringBuilder headersLog = new StringBuilder();
//		Collections.list(request.getHeaderNames()).forEach(headerName ->
//				Collections.list(request.getHeaders(headerName)).forEach(headerValue ->
//						headersLog.append(headerName).append("=").append(headerValue).append("; ")
//				)
//		);