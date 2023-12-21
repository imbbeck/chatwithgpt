package com.nbs.nbs.services.common.NBSXA010_BuildingInfoManagement;

import com.nbs.nbs.entity.buildingInfo.BuildingInfo;
import com.nbs.nbs.entity.buildingInfo.BuildingInfoRepository;
import com.nbs.nbs.entity.buildingInfo.DTO.RegisterBuildingDTO;
import com.nbs.nbs.entity.buildingInfo.DTO.RetrieveAllBuildingsDTO;
import com.nbs.nbs.entity.commonCode.CommonCodeRepository;
import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeNameAndIdDTO;
import com.nbs.nbs.exception.DataNotFoundException;
import com.nbs.nbs.exception.IdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingInfoManagementService {
    private final BuildingInfoRepository buildingInfoRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final String facilityTypeCode = "A020000";

    public List<RetrieveAllBuildingsDTO> retrieveAllBuildings() {
        return buildingInfoRepository.findDTOAllBuildingInfo();
    }

    public RetrieveAllBuildingsDTO retrieveOneBuilding(String buildingId) {
        return buildingInfoRepository.findDTOByBuildingId(buildingId).orElseThrow(() -> new DataNotFoundException("빌딩이 없습니다."));
    }

    public void createBuilding(RegisterBuildingDTO request) {
        if (buildingInfoRepository.existsById(request.getBuildingId())) {
            throw new IdAlreadyExistsException("존재하는 Building ID입니다.");
        }
        BuildingInfo buildingInfo = new BuildingInfo();
        BeanUtils.copyProperties(request, buildingInfo);
        buildingInfoRepository.save(buildingInfo);
    }

    public void updateBuilding(RegisterBuildingDTO request, String buildingId) {
        BuildingInfo buildingInfo = buildingInfoRepository.findById(buildingId).orElseThrow(() -> new DataNotFoundException("빌딩이 없습니다."));
        BeanUtils.copyProperties(request, buildingInfo, "buildingId");
        buildingInfoRepository.save(buildingInfo);
    }

    public List<RetrieveCommonCodeNameAndIdDTO> retrieveAllFacilityType() {
        return commonCodeRepository.findCodeNamesByPrntcodeId(facilityTypeCode);
    }

    public List<RetrieveCommonCodeNameAndIdDTO> retrieveAllSubCommonCode(String commonCodeId) {
        return commonCodeRepository.findCodeNamesByPrntcodeId(commonCodeId);
    }
}
