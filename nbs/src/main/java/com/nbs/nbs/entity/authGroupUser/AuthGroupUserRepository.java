package com.nbs.nbs.entity.authGroupUser;

import com.nbs.nbs.entity.authGroupUser.DTO.AuthGroupUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AuthGroupUserRepository extends JpaRepository<AuthGroupUser, AuthGroupUserId> {
    @Query("SELECT DISTINCT agm.menu.menuId FROM AuthGroupUser agu JOIN agu.authGroup ag JOIN ag.authGroupMenus agm WHERE agu.userId = :userId")
    List<String> findMenuIdsByUserId(@Param("userId") String userId);

    // userId를 기반으로 모든 관련된 authGroupId를 찾는 메서드
    @Query("SELECT agu.authGroupId FROM AuthGroupUser agu WHERE agu.userId = :userId")
    List<String> findAuthGroupIdsByUserId(String userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM AuthGroupUser agu WHERE agu.authGroupId = :authGroupId")
    void deleteByAuthGroupId(@Param("authGroupId") String authGroupId);



}

