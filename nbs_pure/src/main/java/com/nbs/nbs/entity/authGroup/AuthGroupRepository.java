package com.nbs.nbs.entity.authGroup;

import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupDTO;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthGroupRepository extends JpaRepository<AuthGroup,String> {

    @Query("SELECT new com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupsDTO(ag.authGroupId,ag.authGroupName,ag.authGroupDesc,ag.useYn) FROM AuthGroup ag")
    List<RetrieveAuthGroupsDTO> findDTOAll();

    @Query("SELECT new com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupDTO(ag.authGroupId,ag.authGroupName,ag.authGroupDesc,ag.useYn,ag.remark,ag.createdBy,ag.createdAt,ag.updatedBy,ag.updatedAt) FROM AuthGroup ag where upper(ag.useYn) ='Y' and ag.authGroupId = :authGroupId")
    Optional<RetrieveAuthGroupDTO> findDTOAuthGroupById(@Param("authGroupId") String authGroupId);
}
