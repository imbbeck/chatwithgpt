package com.nbs.nbs.services.common.NBSZD020_AuthGroupUserManagement;

import com.nbs.nbs.config.SecurityConfiguration;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "NBSZD020_AuthGroupUserManagement", description = "권한 그룹과 유저 관련 API")
@RestController
@RequestMapping(SecurityConfiguration.MENU_COMMON_PATH + "/authgroupusermanagement")
@AllArgsConstructor
public class AuthGroupUserManagementController {
    private final AuthGroupUserManagementService service;

    // 권한 그룹 목록 조회
    @Operation(
            summary = "권한 그룹 목록 조회",
            description = "시스템에 등록된 모든 권한 그룹의 목록을 조회합니다."
    )
    @GetMapping("/authgroups")
    public ResponseEntity<List<RetrieveAuthGroupsDTO>> retrieveAuthGroups() {
        return ResponseEntity.ok(service.retrieveAuthGroups());
    }

    // 특정 권한 그룹에 속한 사용자 조회
    @Operation(
            summary = "특정 권한 그룹의 사용자 목록 조회",
            description = "주어진 권한 그룹 ID에 속한 사용자들의 목록을 조회합니다."
    )
    @GetMapping("/users/{authGroupId}")
    public ResponseEntity<RetrieveAuthGroupUserResponse> retrieveUsersByAuthGroupId(@PathVariable String authGroupId) {
        return ResponseEntity.ok(service.retrieveUsersByAuthGroupId(authGroupId));
    }

    // 권한 그룹에 사용자 추가 및 삭제
    @Operation(
            summary = "권한 그룹에 사용자 추가 및 삭제",
            description = "특정 권한 그룹에 사용자를 추가하거나 삭제합니다."
    )
    @PostMapping("/users/{authGroupId}")
    public ResponseEntity<Void> addDeleteAuthGroupIdUserId(@PathVariable String authGroupId, @RequestBody AddDeleteAuthGroupIdUserIdRequest request) {
        service.addDeleteAuthGroupIdUserId(authGroupId, request);
        return ResponseEntity.ok().build();
    }


}
