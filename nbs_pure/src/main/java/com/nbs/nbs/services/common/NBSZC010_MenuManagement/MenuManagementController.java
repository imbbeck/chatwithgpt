package com.nbs.nbs.services.common.NBSZC010_MenuManagement;

import com.nbs.nbs.config.SecurityConfiguration;
import com.nbs.nbs.entity.menu.DTO.RegisterMenuDTO;
import com.nbs.nbs.entity.menu.DTO.RetrieveMenuDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "NBSZC010_MenuManagement", description = "메뉴관리 API")
@RestController
@RequestMapping(SecurityConfiguration.MENU_COMMON_PATH + "/menumanagement")
@AllArgsConstructor
public class MenuManagementController {
    private final MenuManagementService service;

    @Operation(
            summary = "시스템별 모든 메뉴 조회",
            description = "주어진 시스템 ID에 해당하는 모든 메뉴를 조회합니다. 시스템ID 예시 BEMS,BAS,CMN"
    )
    @GetMapping("/menus/{systemId}")
    public ResponseEntity<List<RetrieveMenuDTO>> retrieveAllMenus(@PathVariable String systemId) {
        return ResponseEntity.ok(service.retrieveAllMenus(systemId));
    }

    // 메뉴 가져오기
    @Operation(
            summary = "특정 메뉴 조회",
            description = "주어진 메뉴 ID에 해당하는 메뉴의 상세 정보를 조회합니다."
    )
    @GetMapping("/{menuId}")
    public ResponseEntity<RetrieveMenuDTO> retrieveOneMenu(@PathVariable String menuId) {
        return ResponseEntity.ok(service.retrieveOneMenu(menuId));
    }

    // 메뉴 생성
    @Operation(
            summary = "메뉴 생성",
            description = "새로운 메뉴를 생성합니다. 메뉴의 위치는 주어진 메뉴 ID와 부모 메뉴 ID에 따라 결정됩니다. 시스템 메뉴가 BAS인 경우만 추가 및 수정 가능합니다.\"" +
                    "타입은 samelevel, sublevel 가능합니다."
    )
    @PostMapping("/menus/{menuId}/{prntmenuId}/{type}")
    public ResponseEntity<Void> createMenu(
            @PathVariable String menuId,
            @PathVariable String prntmenuId,
            @PathVariable String type,
            @RequestBody RegisterMenuDTO request) {
        service.createMenu(menuId, prntmenuId, type, request);
        return ResponseEntity.ok().build();
    }

    // 상위 메뉴 생성
    @Operation(
            summary = "상위 메뉴 생성",
            description = "새로운 상위 메뉴를 생성합니다. 이 메뉴는 메뉴 계층구조의 최상위에 위치합니다."
    )
    @PostMapping("/supermenus")
    public ResponseEntity<Void> createUpserMenu(@RequestBody RegisterMenuDTO request) {
        service.createSuperMenu(request);
        return ResponseEntity.ok().build();
    }

    // 메뉴 업데이트
    @Operation(
            summary = "메뉴 업데이트",
            description = "기존 메뉴의 정보를 업데이트합니다."
    )
    @PutMapping("/meuns/{menuId}")
    public ResponseEntity<Void> updateMenu(@PathVariable String menuId, @RequestBody RegisterMenuDTO request) {
        service.updateMenu(menuId, request);
        return ResponseEntity.ok().build();
    }

    // 메뉴 삭제
    @Operation(
            summary = "메뉴 삭제",
            description = "주어진 메뉴 ID에 해당하는 메뉴를 삭제합니다."
    )
    @DeleteMapping("/meuns/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable String menuId) {
        service.deleteMenu(menuId);
        return ResponseEntity.ok().build();
    }


}
