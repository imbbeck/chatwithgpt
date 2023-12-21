package com.nbs.nbs.services.common.NBSZB010_UserInfoManagement;

import com.nbs.nbs.config.SecurityConfiguration;
import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeNameAndIdDTO;
import com.nbs.nbs.entity.userInfo.DTO.RegisterRequestUserInfoDTO;
import com.nbs.nbs.entity.userInfo.DTO.RetrieveOneUserInfoDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "NBSZB010_UserInfoManagement", description = "유저 정보 관리 API")
@RestController
@RequestMapping(SecurityConfiguration.MENU_COMMON_PATH + "/usermanagements")
@AllArgsConstructor
public class UserManagementController {

    private final UserManagementService service;

    @Operation(
            summary = "모든 유저 조회",
            description = "시스템에 등록된 모든 유저의 정보를 조회합니다."
    )
    @GetMapping("/users")
    public ResponseEntity<List<RetrieveOneUserInfoDetails>> retrieveAllUsers() {
        return ResponseEntity.ok(service.retrieveAllUsers());
    }

    // 유저 한명 조회하기
    @Operation(
            summary = "특정 유저 조회",
            description = "주어진 ID를 가진 유저의 상세 정보를 조회합니다."
    )
    @GetMapping("/users/{userId}")
    public ResponseEntity<RetrieveOneUserInfoDetails> retrieveOneUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.retrieveOneUser(userId));
    }

    // 유저 등록하기
    @Operation(
            summary = "유저 등록",
            description = "새로운 유저를 시스템에 등록합니다. 관리자 권한이 필요합니다."
    )
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequestUserInfoDTO request) {
        service.registerUser(request);
        return ResponseEntity.ok().build();
    }

    // 유저 업데이트
    @Operation(
            summary = "유저 정보 업데이트",
            description = "기존 유저의 정보를 업데이트합니다."
    )
    @PutMapping("/users/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable String userId, @RequestBody RegisterRequestUserInfoDTO request) {
        service.updateUser(userId, request);
        return ResponseEntity.ok().build();
    }

    // 유저 삭제
    @Operation(
            summary = "유저 삭제",
            description = "주어진 ID를 가진 유저를 시스템에서 삭제합니다."
    )
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        service.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    // 비밀번호 초기화
    @Operation(
            summary = "비밀번호 초기화",
            description = "주어진 ID의 유저 비밀번호를 초기화합니다."
    )
    @PostMapping("/users/{userId}/resetpassword")
    public ResponseEntity<Void> resetUserPassword(@PathVariable String userId) {
        service.resetPassword(userId);
        return ResponseEntity.ok().build();
    }

    // 공통코드에서 직급 가져오기
    @Operation(
            summary = "직급 목록 조회",
            description = "시스템에 등록된 모든 직급의 목록을 조회합니다."
    )
    @GetMapping("/commoncodes/jobtitles")
    public ResponseEntity<List<RetrieveCommonCodeNameAndIdDTO>> retrieveJobTitle() {
        return ResponseEntity.ok(service.retrieveJobTitle());
    }

    @Operation(
            summary = "공통 코드 Id로 하위 코드 List 조회",
            description = "공통 코드 Id로 하위 코드 List 조회"
    )
    @GetMapping("/commoncodes/{commonCodeId}")
    public ResponseEntity<List<RetrieveCommonCodeNameAndIdDTO>> retrieveAllSubCommonCode(@PathVariable String commonCodeId) {
        return ResponseEntity.ok(service.retrieveAllSubCommonCode(commonCodeId));
    }
}
