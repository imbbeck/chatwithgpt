package com.nbs.nbs.services.common.NBSXA010_BuildingInfoManagement;

import com.nbs.nbs.config.SecurityConfiguration;
import com.nbs.nbs.entity.buildingInfo.DTO.RegisterBuildingDTO;
import com.nbs.nbs.entity.buildingInfo.DTO.RetrieveAllBuildingsDTO;
import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeNameAndIdDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "NBSXA010_BuildingInfoManagement", description = "건물 정보 관리 API")
@RestController
@RequestMapping(SecurityConfiguration.MENU_COMMON_PATH + "/buildinginfomanagement")
@AllArgsConstructor
public class BuildingInfoManagementController {
    private final BuildingInfoManagementService service;

    @Operation(
            summary = "모든 빌딩 조회",
            description = "시스템에 등록된 모든 빌딩의 정보를 조회합니다."
    )
    @GetMapping("/buildings")
    public ResponseEntity<List<RetrieveAllBuildingsDTO>> retrieveAllBuildings() {
        return ResponseEntity.ok(service.retrieveAllBuildings());
    }

    @Operation(
            summary = "특정 빌딩 조회",
            description = "주어진 빌딩 ID에 해당하는 빌딩의 상세 정보를 조회합니다."
    )
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<RetrieveAllBuildingsDTO> retrieveOneBuilding(@PathVariable String buildingId) {
        return ResponseEntity.ok(service.retrieveOneBuilding(buildingId));
    }

    @Operation(
            summary = "시설 유형 코드 조회",
            description = "시설 유형에 대한 공통 코드를 조회합니다."
    )
    @GetMapping("/commoncodes/facilityType")
    public ResponseEntity<List<RetrieveCommonCodeNameAndIdDTO>> retrieveAllFacilityType() {
        return ResponseEntity.ok(service.retrieveAllFacilityType());
    }

    @Operation(
            summary = "공통 코드 Id로 하위 코드 List 조회",
            description = "공통 코드 Id로 하위 코드 List 조회"
    )
    @GetMapping("/commoncodes/{commonCodeId}")
    public ResponseEntity<List<RetrieveCommonCodeNameAndIdDTO>> retrieveAllSubCommonCode(@PathVariable String commonCodeId) {
        return ResponseEntity.ok(service.retrieveAllSubCommonCode(commonCodeId));
    }

    @Operation(
            summary = "새로운 빌딩 등록",
            description = "새로운 빌딩 정보를 시스템에 등록합니다."
    )
    @PostMapping("/building")
    public ResponseEntity<Void> createBuilding(@RequestBody RegisterBuildingDTO request) {
        service.createBuilding(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "빌딩 정보 업데이트",
            description = "주어진 빌딩 ID에 해당하는 빌딩의 정보를 업데이트합니다."
    )
    @PutMapping("/building/{buildingId}")
    public ResponseEntity<Void> updateBuilding(
            @RequestBody RegisterBuildingDTO request,
            @PathVariable String buildingId) {
        service.updateBuilding(request, buildingId);
        return ResponseEntity.ok().build();
    }
}

