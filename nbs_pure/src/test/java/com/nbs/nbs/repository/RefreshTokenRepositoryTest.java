package com.nbs.nbs.repository;

import com.nbs.nbs.entity.refreshToken.RefreshToken;
import com.nbs.nbs.entity.refreshToken.RefreshTokenRepository;
import com.nbs.nbs.entity.userInfo.UserInfo;
import com.nbs.nbs.entity.userInfo.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RefreshTokenRepositoryTest extends BaseRepositoryTest<RefreshToken, Integer> {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected RefreshToken createEntity() {
        Integer Id = integerId();
        RefreshToken entity = RefreshToken.builder()
                .tokenIdx(Id)
                .refreshToken("iamtoken")
                .expiredYn("Y")
                .validYn("Y")
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")
                .build();
        return entity;
    }

    @Override
    protected Integer getId(RefreshToken entity) {
        return entity.getTokenIdx();
    }
// 추가 테스트 케이스 아래에 작성

    @Transactional
    @Test
    public void testFindAllByUserId() {
        // UserInfo 인스턴스 생성
        String userId = stringId();
        UserInfo userInfo = UserInfo.builder()
                .userId(userId)
                .userPwd("encryptedPassword")
                .userName("UserName")
                .delYn("N")
                .updatedAt(LocalDateTime.now())
                .build();
        // UserInfo 저장
        userInfoRepository.save(userInfo);

        // RefreshToken 인스턴스 생성
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString()) // UUID 사용
                .userInfo(userInfo) // UserInfo와 연결
                .validYn("Y")
                .expiredYn("N")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("system")
                .updatedBy("system")
                .build();
        // refresh토큰 저장
        refreshTokenRepository.save(refreshToken);
        userInfo.setRefreshTokens(Set.of(refreshToken));
        userInfoRepository.save(userInfo);
        // UserInfo 조회
        UserInfo foundUserInfo = userInfoRepository.findById(userId).orElseThrow();
        Set<RefreshToken> refreshTokens = foundUserInfo.getRefreshTokens();

        List<RefreshToken> targetToken = refreshTokenRepository.findAllByUserId(userId);

        boolean setsAreEqual = refreshTokens.size() == targetToken.size() &&
                refreshTokens.containsAll(targetToken) &&
                targetToken.containsAll(refreshTokens);
// 검증
        assertTrue(setsAreEqual);
    }

    @Transactional
    @Test
    public void testfindValidTokenByUserId() {
        // UserInfo 인스턴스 생성
        String userId = stringId();
        UserInfo userInfo = UserInfo.builder()
                .userId(userId)
                .userPwd("encryptedPassword")
                .userName("UserName")
                .delYn("N")
                .updatedAt(LocalDateTime.now())
                .build();
        // UserInfo 저장
        userInfoRepository.save(userInfo);

        // RefreshToken 인스턴스 생성
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString()) // UUID 사용
                .userInfo(userInfo) // UserInfo와 연결
                .validYn("Y")
                .expiredYn("N")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("system")
                .updatedBy("system")
                .build();

        RefreshToken refreshTokenValidN = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString()) // UUID 사용
                .userInfo(userInfo) // UserInfo와 연결
                .validYn("N")
                .expiredYn("N")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("system")
                .updatedBy("system")
                .build();

        RefreshToken refreshTokenValidNAndExpriedY = RefreshToken.builder()
                .refreshToken(UUID.randomUUID().toString()) // UUID 사용
                .userInfo(userInfo) // UserInfo와 연결
                .validYn("N")
                .expiredYn("Y")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("system")
                .updatedBy("system")
                .build();


        refreshTokenRepository.saveAll(List.of(refreshTokenValidN,refreshToken,refreshTokenValidNAndExpriedY));
        userInfo.setRefreshTokens(Set.of(refreshToken,refreshTokenValidN,refreshTokenValidNAndExpriedY));
        userInfoRepository.save(userInfo);
        UserInfo foundUserInfo = userInfoRepository.findById(userId).orElseThrow();
        Set<RefreshToken> refreshTokens = foundUserInfo.getRefreshTokens();

        boolean setsAreEqual = refreshTokens.size() == 3;
        RefreshToken targetToken = refreshTokenRepository.findValidTokenByUserId(userId).orElseThrow();


// 검증
        assertTrue(setsAreEqual);
        assertThat(targetToken.getValidYn().toUpperCase()).isEqualTo("Y");
        assertThat(targetToken.getExpiredYn().toUpperCase()).isEqualTo("N");
    }

    @Transactional
    @Test
    public void testfindByRefreshToken() {
        // refresh 토큰 저장 후 uuid로 조회
        String tokenUUID = UUID.randomUUID().toString();
        // RefreshToken 인스턴스 생성
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(tokenUUID) // UUID 사용
                .validYn("Y")
                .expiredYn("N")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("system")
                .updatedBy("system")
                .build();
        refreshTokenRepository.save(refreshToken);

        var targetRefreshToken = refreshTokenRepository.findByRefreshToken(tokenUUID);
        boolean targetRefreshTokenPresent = targetRefreshToken.isPresent();


// 검증
        assertTrue(targetRefreshTokenPresent);
        assertThat(targetRefreshToken.get().getRefreshToken()).isEqualTo(tokenUUID);
    }

}
