package com.nbs.nbs.config;

import com.nbs.nbs.entity.refreshToken.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// 리프레시 토큰 요청시에 데이터베이스에 있는 유효한 토큰이 있는지 비교하는 필터
@Component
@RequiredArgsConstructor
public class RefreshTokenAuthenticationFilter extends OncePerRequestFilter {
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain
	) throws ServletException, IOException {

		String path = request.getRequestURI();
		List<String> refreshTokenPaths = Arrays.asList(SecurityConfiguration.REFRESH_TOKEN_PATH);

		if (refreshTokenPaths.contains(path)) {
			final String authHeader = request.getHeader("Authorization");
			String jwt = authHeader.substring(7);
			boolean savedToken = refreshTokenRepository.findByRefreshToken(jwt)
					.map(rt -> "N".equals(rt.getExpiredYn()) && "Y".equals(rt.getValidYn()))
					.orElse(false);

			if (savedToken) {
				filterChain.doFilter(request, response);
            } else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            return;
        }

		filterChain.doFilter(request, response);
	}
}
