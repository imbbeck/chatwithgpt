package com.nbs.nbs.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JwtService {

    // JwtEncoder를 주입받습니다. 이는 JWT를 인코딩하는 데 사용됩니다.
    private final JwtEncoder jwtEncoder;

    // application.properties 파일로부터 JWT의 만료 시간을 주입받습니다.
    @Value("${application.security.jwt.expiration}")
    private long accessExpiration;

    // application.properties 파일로부터 리프레쉬 토큰의 만료 시간을 주입받습니다.
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    String accessType = "ACCESS";
    String refreshType = "REFRESH";

    // 액세스 토큰을 생성하는 메소드입니다.
    public String generateToken(Authentication authentication) {
        return buildToken(authentication, accessExpiration, accessType);
    }

    // 리프레쉬 토큰을 생성하는 메소드입니다.
    public String generateRefreshToken(Authentication authentication) {
        return buildToken(authentication, refreshExpiration, refreshType);
    }

    // JWT를 구성하고 인코딩하는 메소드입니다.
    private String buildToken(Authentication authentication, long expiration, String type) {
        var claims = JwtClaimsSet.builder()
                .issuer("self") // 토큰 발행자
                .issuedAt(Instant.now()) // 발행 시간
                .expiresAt(Instant.now().plusMillis(expiration)) // 만료 시간
                .subject(authentication.getName()) // 토큰 주체
                .claim("scope", type)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String refreshRequestGenerateAccessToken(Authentication authentication) {

        return refreshRequestBuildToken(authentication, accessExpiration, accessType);

    }

    public String refreshRequestGenerateRefreshToken(Authentication authentication) {

        return refreshRequestBuildToken(authentication, refreshExpiration, refreshType);
    }

    private String refreshRequestBuildToken(Authentication authentication, long expiration, String type) {
        var claims = JwtClaimsSet.builder()
                .issuer("self") // 토큰 발행자
                .issuedAt(Instant.now()) // 발행 시간
                .expiresAt(Instant.now().plusMillis(expiration)) // 만료 시간
                .subject(authentication.getName()) // 토큰 주체
                .claim("scope", refreshRequestCreateScope(authentication, type)) // 'scope' 클레임에 권한과 토큰 타입 추가
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private List<String> refreshRequestCreateScope(Authentication authentication, String type) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("SCOPE_", "")) // "SCOPE_" 제거
                .filter(role -> !role.equals("REFRESH")) // "REFRESH"가 아닌 역할만 포함
                .collect(Collectors.toList());
        roles.add(type); // 토큰 타입을 'scope' 클레임에 추가
        return roles;
    }
}

