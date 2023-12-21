package com.nbs.nbs.services.common.NBSZD030_AuthGroupMenuManagement;

import com.nbs.nbs.config.SecurityConfiguration;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupsDTO;
import com.nbs.nbs.entity.menu.DTO.MenuAuthGroupDTO;
import com.nbs.nbs.entity.systemApplication.DTO.RetrieveSystemApplicationNameDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "NBSZD030_AuthGroupMenuManagement", description = "권한 그룹과 메뉴 관련 API")
@RestController
@RequestMapping(SecurityConfiguration.MENU_COMMON_PATH + "/authgroupmenumanagement")
@AllArgsConstructor
public class AuthGroupMenuManagementController {
    private final AuthGroupMenuManagementService service;

    // 사용 가능한 권한 그룹 조회
    @Operation(
            summary = "사용 가능한 권한 그룹 조회",
            description = "시스템에 등록된 모든 사용 가능한 권한 그룹을 조회합니다."
    )
    @GetMapping("/authgroups")
    public ResponseEntity<List<RetrieveAuthGroupsDTO>> retrieveAllAuthGroups() {
        return ResponseEntity.ok(service.retrieveAllAuthGroups());
    }

    // 특정 권한 그룹과 시스템 ID에 따른 메뉴 조회
    @Operation(
            summary = "특정 권한 그룹과 시스템 ID에 따른 메뉴 조회",
            description = "주어진 권한 그룹 ID와 시스템 ID에 따라 사용 가능한 메뉴들을 조회합니다."
    )
    @GetMapping("/menus/authgroup/{authGroupId}/systemapplications/{systemId}")
    public ResponseEntity<List<MenuAuthGroupDTO>> retrieveAllMenusByAuthGroupIdAndSystemId(
            @PathVariable String authGroupId,
            @PathVariable String systemId) {
        return ResponseEntity.ok(service.retrieveAllUsersByAuthGroupId(authGroupId, systemId));
    }

    // 시스템 리스트 조회
    @Operation(
            summary = "시스템 리스트 조회",
            description = "사용 가능한 모든 시스템의 이름을 조회합니다."
    )
    @GetMapping("/systemapplications")
    public ResponseEntity<List<RetrieveSystemApplicationNameDTO>> retrieveAllSystemApplicationNameByUseYn() {
        return ResponseEntity.ok(service.retrieveAllSystemApplicationNameByUseYn());
    }

    // 권한 그룹에 메뉴 추가
    @Operation(
            summary = "권한 그룹에 메뉴 추가",
            description = "특정 권한 그룹에 새로운 메뉴를 추가합니다."
    )
    @PostMapping("/authgroup/{authGroupId}")
    public ResponseEntity<Void> addAuthGroupIdMenuId(
            @PathVariable String authGroupId,
            @RequestBody AddAuthGroupIdMenuIdRequest request) {
        service.addAuthGroupIdMenuId(authGroupId, request);
        return ResponseEntity.ok().build();
    }


}
