package com.nbs.nbs.services.common.NBSZD010_AuthGroupManagement;

import com.nbs.nbs.config.SecurityConfiguration;
import com.nbs.nbs.entity.authGroup.DTO.RegisterAuthGroupDTO;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupDTO;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "NBSZD010_AuthGroupManagement", description = "권한 그룹 관리 API")
@RestController
@RequestMapping(SecurityConfiguration.MENU_COMMON_PATH + "/authgroupmanagement")
@AllArgsConstructor
public class AuthGroupManagementController {

    private final AuthGroupManagementService service;

    // 권한그룹들 가져오기
    @Operation(
            summary = "모든 권한 그룹 조회",
            description = "시스템에 등록된 모든 권한 그룹의 정보를 조회합니다."
    )
    @GetMapping("/authgroups")
    public ResponseEntity<List<RetrieveAuthGroupsDTO>> retrieveAllAuthGroups() {
        return ResponseEntity.ok(service.retrieveAllAuthGroups());
    }

    // 권한그룹 가져오기
    @Operation(
            summary = "특정 권한 그룹 조회",
            description = "주어진 권한 그룹 ID에 해당하는 권한 그룹의 상세 정보를 조회합니다."
    )
    @GetMapping("/authgroups/{authgroupId}")
    public ResponseEntity<RetrieveAuthGroupDTO> retrieveOneAuthGroup(@PathVariable String authgroupId) {
        return ResponseEntity.ok(service.retrieveOneAuthGroup(authgroupId));
    }

    // 권한그룹 생성
    @Operation(
            summary = "권한 그룹 생성",
            description = "새로운 권한 그룹을 생성합니다."
    )
    @PostMapping("/authgroups")
    public ResponseEntity<Void> createAUthGroup(@RequestBody RegisterAuthGroupDTO request) {
        service.createAUthGroup(request);
        return ResponseEntity.ok().build();
    }

    // 권한그룹 업데이트
    @Operation(
            summary = "권한 그룹 업데이트",
            description = "기존 권한 그룹의 정보를 업데이트합니다."
    )
    @PutMapping("/authgroups/{authgroupId}")
    public ResponseEntity<Void> updateAuthGroup(@PathVariable String authgroupId, @RequestBody RegisterAuthGroupDTO request) {
        service.updateAuthGroup(authgroupId, request);
        return ResponseEntity.ok().build();
    }

    // 권한그룹 삭제
    @Operation(
            summary = "권한 그룹 삭제",
            description = "주어진 권한 그룹 ID에 해당하는 권한 그룹을 삭제합니다."
    )
    @DeleteMapping("/authgroups/{authgroupId}")
    public ResponseEntity<Void> deleteAuthGroup(@PathVariable String authgroupId) {
        service.deleteAuthGroup(authgroupId);
        return ResponseEntity.ok().build();
    }


}
