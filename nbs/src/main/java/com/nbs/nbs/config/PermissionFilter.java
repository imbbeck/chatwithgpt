package com.nbs.nbs.config;

import com.nbs.nbs.entity.authGroupUser.AuthGroupUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PermissionFilter extends OncePerRequestFilter {

    private final AuthGroupUserRepository authGroupUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean isMenuPermissionPath = path.startsWith(SecurityConfiguration.MENU_PATH);
        if (isMenuPermissionPath) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String menuId = request.getHeader("menu");
            if (authentication != null) {
                String userId = authentication.getName();
                List<String> menuList = authGroupUserRepository.findMenuIdsByUserId(userId);
                Optional<String> menuOptional = menuList.stream()
                        .filter(menu -> menu.equals(menuId))
                        .findFirst();

                if (menuOptional.isPresent()) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
