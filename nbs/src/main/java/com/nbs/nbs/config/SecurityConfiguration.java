package com.nbs.nbs.config;

import com.nbs.nbs.services.common.NBSZF010_Logging.MenuDeniedLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final BasicAuthenticationFilter basicAuthenticationFilter;
    private final RefreshTokenAuthenticationFilter refreshTokenAuthenticationFilter;
    private final PermissionFilter permissionFilter;
    private final AdminRoleFilter adminRoleFilter;
    private final MenuDeniedLoggingInterceptor menuDeniedLoggingInterceptor;

    public static final String MENU_PATH = "/api/v1/menu";
    public static final String AUTH_PATH = "/api/v1/auth";
    public static final String MENU_COMMON_PATH = MENU_PATH + "/common";
    public static final String MENU_BEMS_PATH = MENU_PATH + "/bems";
    public static final String MENU_BAS_PATH = MENU_PATH + "/bas";
    public static final String LOGOUT_PATH = AUTH_PATH+"/logout";
    private static final String[] WHITE_LIST_URL = {
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/doc/**",
            // h2 - console
            "/h2-console/**"};

    static final String[] BASIC_AUTH_LIST = {
            AUTH_PATH+"/authenticate"
    };

    static final String[] REFRESH_TOKEN_PATH = {
            AUTH_PATH+"/refresh-token"
    };

    static final String[] ADMIN_ROLE_PATH_LIST = {
            MENU_COMMON_PATH + "/usermanagements/register",
            AUTH_PATH+"/register"
    };

    // HTTP 보안을 구성하는 빈을 정의합니다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 보호 기능을 비활성화합니다.
        http.csrf().disable();
        http.cors();

        http.authorizeHttpRequests(auth -> {

            // 기존 코드 (spring boot 3.1이상에서만 가능
            // .requestMatchers(BASIC_AUTH_LIST)
            // .permitAll()
            // .requestMatchers(REFRESH_TOKEN_PATH)
            // .permitAll()
            // .requestMatchers(ADMIN_ROLE_PATH_LIST)
            // .permitAll()
            // .requestMatchers(WHITE_LIST_URL)
            // .permitAll()
            // .requestMatchers(LOGOUT_PATH)
            // .permitAll()
            // .requestMatchers("/**").hasAuthority("SCOPE_ACCESS")
            // .anyRequest().authenticated()

            // for h2 database ..
            List<RequestMatcher> basicAuthMatchers = someListMatcher(Arrays.asList(BASIC_AUTH_LIST));
            List<RequestMatcher> refreshTokenMatchers = someListMatcher(Arrays.asList(REFRESH_TOKEN_PATH));
            List<RequestMatcher> whiteListMatchers = someListMatcher(Arrays.asList(WHITE_LIST_URL));
            List<RequestMatcher> logoutPathMatchers = someListMatcher(Arrays.asList(LOGOUT_PATH));
            List<RequestMatcher> adminPathMatchers = someListMatcher(Arrays.asList(ADMIN_ROLE_PATH_LIST));

            // 각 URL 패턴에 대한 보안 규칙 설정
            auth.requestMatchers(basicAuthMatchers.toArray(new RequestMatcher[0])).permitAll();
            auth.requestMatchers(refreshTokenMatchers.toArray(new RequestMatcher[0])).permitAll();
            auth.requestMatchers(whiteListMatchers.toArray(new RequestMatcher[0])).permitAll();
            auth.requestMatchers(logoutPathMatchers.toArray(new RequestMatcher[0])).permitAll();
            auth.requestMatchers(adminPathMatchers.toArray(new RequestMatcher[0])).permitAll();
            auth.requestMatchers(new AntPathRequestMatcher("/**")).hasAuthority("SCOPE_ACCESS");
            auth.anyRequest().authenticated();

        });
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        http.addFilterBefore(basicAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(refreshTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(adminRoleFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterAfter(permissionFilter, BearerTokenAuthenticationFilter.class);
        // menu 접근 권한이 없을 경우 로그를 남기기 위한 interceptor
        http.exceptionHandling().accessDeniedHandler(menuDeniedLoggingInterceptor);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().sameOrigin();
        return http.build();
    }

    // h2 database 사용을 위한 method
    List<RequestMatcher> someListMatcher(List<String> someList) {
        return someList.stream()
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
    }
}
