package com.nbs.nbs.services.common.NBSZA010_CommonCodeManagement;

import com.nbs.nbs.config.SecurityConfiguration;
import com.nbs.nbs.entity.commonCode.DTO.RegisterCommonCodeDTO;
import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "NBSZA010_CommonCodeManagement", description = "공통코드관리 API")
@RestController
@RequestMapping(SecurityConfiguration.MENU_COMMON_PATH + "/commoncodes")
@AllArgsConstructor
public class CommonCodeManagementController {

    private final CommonCodeManagementService service;

    // 상위코드 가져오기
    @Operation(
            summary = "모든 코드 조회",
            description = "시스템에 등록된 모든 공통코드를 조회합니다."
    )
    @GetMapping("/codes")
    public ResponseEntity<List<RetrieveCommonCodeDTO>> retrieveAllCodes() {
        return ResponseEntity.ok(service.retrieveAllCodes());
    }

    // 상위코드에 연관된 하위코드 가져오기
    // 트리는 프론트에서 가져오기로 해서 일단 주석
//    @GetMapping("/supercodes/{superCodeId}/subcodes")
//    public ResponseEntity<List<RetrieveCommonCodeDTO>> retrieveCodesBySuperCodeID(@PathVariable String superCodeId) {
//        return ResponseEntity.ok(service.retrieveCodesBySuperCodeID(superCodeId));
//    }

    // 코드생성 변수에 해당하는 값은 기준에 해당하는 코드. 누구 기준 하위레벨 ? 누구 기준 같은 레벨? type은 sublevel과 samelevel 두가지만 가능.
    @Operation(
            summary = "공통코드 생성",
            description = "새로운 공통코드를 생성합니다. 기준코드의 codeId와 prntcodeId를 기준으로 'samelevel' 또는 'sublevel' 유형의 코드를 생성할 수 있습니다. 'samelevel'은 동일 레벨의 코드를, 'sublevel'은 하위 레벨의 코드를 생성합니다."
    )
    @PostMapping("/{codeId}/{prntcodeId}/{type}")
    public ResponseEntity<Void> createCode(
            @PathVariable String codeId,
            @PathVariable String prntcodeId,
            @PathVariable String type,
            @RequestBody RegisterCommonCodeDTO request) {
        service.createCode(request, codeId, prntcodeId, type);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "공통코드 업데이트",
            description = "지정된 공통코드의 정보를 업데이트합니다."
    )
    @Transactional
    @PutMapping("/{codeId}/{prntcodeId}")
    public ResponseEntity<Void> updateCommonCode(
            @PathVariable String codeId,
            @PathVariable String prntcodeId,
            @RequestBody RegisterCommonCodeDTO request
    ) {
        service.updateCommonCode(codeId, prntcodeId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "상위코드 삭제",
            description = "지정된 상위코드를 삭제합니다. 해당 코드에 하위코드가 존재하는 경우 삭제할 수 없습니다."
    )
    @Transactional
    @DeleteMapping("/supercodes/{supercodeId}")
    public ResponseEntity<Void> deleteSuperCode(@PathVariable String supercodeId) {
        service.deleteSuperCode(supercodeId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "하위코드 일괄 삭제",
            description = "지정된 하위코드들을 일괄적으로 삭제합니다. 상위코드가 포함된 경우 삭제할 수 없습니다."
    )
    @Transactional
    @DeleteMapping("/subcodes")
    public ResponseEntity<Void> deleteSubCodes(@RequestBody List<String> codeIds) {
        service.deleteSubCodes(codeIds);
        return ResponseEntity.ok().build();
    }
}
