package com.nbs.nbs.entity.authGroupMenu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthGroupMenuRepository extends JpaRepository<AuthGroupMenu,AuthGroupMenuId> {

    @Transactional
    @Modifying
    @Query("DELETE FROM AuthGroupMenu agm WHERE agm.authGroupId = :authGroupId")
    void deleteByAuthGroupId(String authGroupId);
}

