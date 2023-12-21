package com.nbs.nbs.entity.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    // 특정 사용자의 리프레시 토큰 찾기
    @Query("SELECT r FROM RefreshToken r WHERE r.userInfo.userId = :userId")
    List<RefreshToken> findAllByUserId(String userId);

    // 유효한 리프레시 토큰 찾기
    @Query("SELECT r FROM RefreshToken r WHERE r.userInfo.userId = :userId AND upper(r.expiredYn) = 'N' AND upper(r.validYn) = 'Y'")
    Optional<RefreshToken> findValidTokenByUserId(String userId);



    // 리프레시 토큰으로 찾기
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
