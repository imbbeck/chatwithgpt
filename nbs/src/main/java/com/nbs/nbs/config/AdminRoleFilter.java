package com.nbs.nbs.config;

import com.nbs.nbs.entity.authGroupUser.AuthGroupUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class AdminRoleFilter extends OncePerRequestFilter {

    private final AuthGroupUserRepository authGroupUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        List<String> adminRolePathList = Arrays.asList(SecurityConfiguration.ADMIN_ROLE_PATH_LIST);
        if (adminRolePathList.contains(path)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                String adminRole = "ADMIN";
                String userId = authentication.getName();
                List<String> authGroupList = authGroupUserRepository.findAuthGroupIdsByUserId(userId);
                if (authGroupList.contains(adminRole)) {
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
