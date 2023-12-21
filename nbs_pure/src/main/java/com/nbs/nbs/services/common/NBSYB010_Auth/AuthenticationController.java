package com.nbs.nbs.services.common.NBSYB010_Auth;

import com.nbs.nbs.config.SecurityConfiguration;
import com.nbs.nbs.entity.systemApplication.DTO.RetrieveSystemApplicationNameDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "NBSYB010_Auth", description = "로그인 로그아웃 재인증")
@RestController
@RequestMapping(SecurityConfiguration.AUTH_PATH)
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @Operation(
            description = "Basic auth로 인증을 한다음에 요청하세요",
            summary = "only basic auth"
    )
    @PostMapping("/authenticate")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<AuthenticationResponse> authenticate(Authentication authentication) {
        return ResponseEntity.ok(service.authenticate(authentication));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리")
    public ResponseEntity<?> logout(Authentication authentication) {
        service.logout(authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "refresh token으로 재인증 해야 요청할 수 있습니다. ")
    @PostMapping("/refresh-token")
    @PreAuthorize("hasAuthority('SCOPE_REFRESH')")
    public ResponseEntity<AuthenticationResponse> refreshToken(Authentication authentication) {
        return ResponseEntity.ok(service.refreshToken(authentication));
    }

    // 로그인시에 어떤 어플리케이션을 선택할 건지 고르는 화면
    @GetMapping("/systemapplications")
    public ResponseEntity<List<RetrieveSystemApplicationNameDTO>> retrieveAllSystemApplicationNameByUseYn() {
        return ResponseEntity.ok(service.retrieveAllSystemApplicationNameByUseYn());
    }
}

