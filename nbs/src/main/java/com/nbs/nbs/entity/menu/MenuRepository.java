package com.nbs.nbs.entity.menu;

import com.nbs.nbs.entity.menu.DTO.MenuAuthGroupDTO;
import com.nbs.nbs.entity.menu.DTO.RetrieveMenuDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, String> {

    boolean existsByPrntmenuId(String prntmenuId);

    @Query("SELECT new com.nbs.nbs.entity.menu.DTO.RetrieveMenuDTO(" +
            "s.applicationName, m.menuId, m.menuName, m.sortOdr, m.prntmenuId, m.menuUrl, " +
            "m.remark, m.useYn,m.useYn, m.createdBy, m.createdAt, m.updatedBy, m.updatedAt) " +
            "FROM Menu m JOIN m.systemApplication s WHERE m.menuId = :menuId")
    Optional<RetrieveMenuDTO> findDTOMenuByMenuId(@Param("menuId") String menuId);

    @Query("SELECT new com.nbs.nbs.entity.menu.DTO.RetrieveMenuDTO(" +
            "s.applicationName, m.menuId, m.menuName, m.sortOdr, m.prntmenuId, m.menuUrl, " +
            "m.remark, m.useYn,m.useYn, m.createdBy, m.createdAt, m.updatedBy, m.updatedAt) " +
            "FROM Menu m JOIN m.systemApplication s where s.applicationId = :systemId or s.applicationId = 'CMN'")
    List<RetrieveMenuDTO> findDTOAllMenu(@Param("systemId") String systemId);

    @Query("SELECT new com.nbs.nbs.entity.menu.DTO.MenuAuthGroupDTO(m.menuId, m.menuName, auth.authGroupId) " +
            "FROM Menu m LEFT JOIN AuthGroupMenu auth ON m.menuId = auth.menuId AND auth.authGroupId = :groupId " +
            "JOIN m.systemApplication s WHERE s.applicationId = :systemId")
    List<MenuAuthGroupDTO> findDTOMenusByAuthGroupIdAndSystemId(@Param("groupId") String groupId, @Param("systemId") String systemId);

}
