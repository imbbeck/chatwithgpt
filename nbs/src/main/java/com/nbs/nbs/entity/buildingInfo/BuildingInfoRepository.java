package com.nbs.nbs.entity.buildingInfo;

import com.nbs.nbs.entity.buildingInfo.DTO.RetrieveAllBuildingsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BuildingInfoRepository extends JpaRepository<BuildingInfo, String> {

    @Query("SELECT new com.nbs.nbs.entity.buildingInfo.DTO.RetrieveAllBuildingsDTO(b.buildingId, b.buildingName, b.facilityNumber, b.facilityType,c.codeName, b.buildingDivision, b.buildingZipcode, b.buildingAddress, b.buildingAddressDetail, b.buildingOwner, b.completionDate, b.warrantyDate, b.safetyCheck, b.maintenance, b.designStart, b.designEnd, b.designer, b.constructionStart, b.constructionEnd, b.constructor, b.totalCost, b.superviseStart, b.superviseEnd, b.supervisor, b.projectOwner, b.projectName, b.inspector, b.photoFile1, b.photoFile2, b.remark, b.createdBy, b.createdAt, b.updatedBy, b.updatedAt) FROM BuildingInfo b LEFT JOIN CommonCode c ON b.facilityType = c.codeId")
    List<RetrieveAllBuildingsDTO> findDTOAllBuildingInfo();

    @Query("SELECT new com.nbs.nbs.entity.buildingInfo.DTO.RetrieveAllBuildingsDTO(" +
            "b.buildingId, b.buildingName, b.facilityNumber, b.facilityType, c.codeName,b.buildingDivision, " +
            "b.buildingZipcode, b.buildingAddress, b.buildingAddressDetail, b.buildingOwner, " +
            "b.completionDate, b.warrantyDate, b.safetyCheck, b.maintenance, b.designStart, " +
            "b.designEnd, b.designer, b.constructionStart, b.constructionEnd, b.constructor, " +
            "b.totalCost, b.superviseStart, b.superviseEnd, b.supervisor, b.projectOwner, " +
            "b.projectName, b.inspector, b.photoFile1, b.photoFile2, b.remark, b.createdBy, " +
            "b.createdAt, b.updatedBy, b.updatedAt) " +
            "FROM BuildingInfo b LEFT JOIN CommonCode c ON b.facilityType = c.codeId WHERE b.buildingId = :buildingId")
    Optional<RetrieveAllBuildingsDTO> findDTOByBuildingId(@Param("buildingId") String buildingId);


}
