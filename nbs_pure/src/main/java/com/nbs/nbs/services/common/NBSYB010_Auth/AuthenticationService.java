package com.nbs.nbs.services.common.NBSYB010_Auth;

import com.nbs.nbs.config.JwtService;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUserRepository;
import com.nbs.nbs.entity.refreshToken.RefreshToken;
import com.nbs.nbs.entity.refreshToken.RefreshTokenRepository;
import com.nbs.nbs.entity.systemApplication.DTO.RetrieveSystemApplicationNameDTO;
import com.nbs.nbs.entity.systemApplication.SystemApplicationRepository;
import com.nbs.nbs.entity.userInfo.UserInfo;
import com.nbs.nbs.entity.userInfo.UserInfoRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    // 필요한 의존성들을 주입합니다. final로 선언된 필드에 대한 생성자 주입이 이루어집니다.
    private final UserInfoRepository userInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthGroupUserRepository authGroupUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SystemApplicationRepository systemApplicationRepository;


    // 사용자의 새 토큰을 저장하는 메소드입니다.
    private void saveUserToken(UserInfo user, String jwtToken) {
        var token = RefreshToken.builder()
                .userInfo(user)
                .refreshToken(jwtToken)
                .expiredYn("N")
                .validYn("Y")
                .build();
        refreshTokenRepository.save(token);
    }

    // 사용자 인증 및 토큰 발급 로직을 처리하는 메소드입니다.
    public AuthenticationResponse authenticate(Authentication authentication) {
        // JWT 토큰을 생성합니다.
        var jwtToken = jwtService.generateToken(authentication);
        // 리프레시 토큰을 생성합니다.
        var refreshToken = jwtService.generateRefreshToken(authentication);
        final String username = authentication.getName();
        updateOrSaveRefreshToken(username, refreshToken);
        // 인증 응답 객체를 생성하고 반환합니다.
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void updateOrSaveRefreshToken(String userId, String refreshToken) {
        List<RefreshToken> storedTokens = refreshTokenRepository.findAllByUserId(userId);

        if (!storedTokens.isEmpty()) {
            // 첫 번째 토큰 업데이트
            RefreshToken firstToken = storedTokens.get(0);
            updateRefreshToken(refreshToken, firstToken);

            // 나머지 토큰들 무효화
            if (storedTokens.size() > 1) {
                storedTokens.subList(1, storedTokens.size()).forEach(this::invalidateToken);
            }
        } else {
            var user = getUser(userId).orElseThrow(
                    () -> new UsernameNotFoundException("User not found with username: " + userId));
            saveUserToken(user, refreshToken);
        }
    }

    private void invalidateToken(RefreshToken token) {
        token.setValidYn("N"); // 유효성을 'N'으로 설정
        refreshTokenRepository.save(token); // 변경 사항 저장
    }


    // 리프레시 토큰을 통한 인증 토큰 갱신 로직을 처리하는 메소드입니다.
    public AuthenticationResponse refreshToken(Authentication authentication) {
        final String username = authentication.getName();
        // 새 액세스 토큰 및 리프레시 토큰을 생성합니다.
        var accessToken = jwtService.refreshRequestGenerateAccessToken(authentication);
        var refreshToken = jwtService.refreshRequestGenerateRefreshToken(authentication);
        // token update
        updateOrSaveRefreshToken(username, refreshToken);

        // 인증 응답 객체를 생성하고 반환합니다.
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private Optional<UserInfo> getUser(String userId) {
        return this.userInfoRepository.findById(userId);
    }

    private void updateRefreshToken(String newToken, RefreshToken pastToken) {
        pastToken.setRefreshToken(newToken); // 새 토큰 값으로 업데이트
        pastToken.setValidYn("Y"); // expired를 false로 설정
        pastToken.setExpiredYn("N"); // revoked를 false로 설정
        refreshTokenRepository.save(pastToken); // 업데이트된 토큰 저장

    }

    void logout(Authentication authentication) {
        try {
            var username = authentication.getName();
            Optional<RefreshToken> optionalToken = refreshTokenRepository.findValidTokenByUserId(username);
            if (optionalToken.isPresent()) {
                RefreshToken token = optionalToken.get();
                token.setExpiredYn("Y");
                token.setValidYn("N");
                refreshTokenRepository.save(token); // 토큰 상태 변경을 저장
            }
        } catch (Exception e) {
            // 로그 기록 또는 다른 오류 처리
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    public List<RetrieveSystemApplicationNameDTO> retrieveAllSystemApplicationNameByUseYn() {
        return systemApplicationRepository.findDTOByUseYn();
    }
}
