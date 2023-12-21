package com.nbs.nbs;

import com.nbs.nbs.entity.authGroupUser.AuthGroupUserId;
import com.nbs.nbs.services.common.NBSYB010_Auth.AuthenticationService;
import com.nbs.nbs.entity.authGroup.AuthGroupRepository;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenu;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenuRepository;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUser;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUserRepository;
import com.nbs.nbs.entity.menu.MenuRepository;
import com.nbs.nbs.entity.userInfo.UserInfo;
import com.nbs.nbs.entity.userInfo.UserInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@SpringBootApplication
public class NbsApplication {
    private final AuthGroupUserRepository authGroupUserRepository;
    private final AuthGroupRepository authGroupRepository;
    private final MenuRepository menuRepository;
    private final AuthGroupMenuRepository authGroupMenuRepository;
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;


    public static void main(String[] args) {
        SpringApplication.run(NbsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService service
    ) {
        return args -> {
            var user = UserInfo.builder()
                    .userId("Admin")
                    .userName("admin")
                    .email("admin@tsm.co.kr")
                    .userPwd(passwordEncoder.encode("admin"))
                    //.authGroupUsers(authGroupListOptional)
                    .build();
            var userManager = UserInfo.builder()
                    .userId("Manager")
                    .userName("manager")
                    .email("manager.tsm.co.kr")
                    .userPwd(passwordEncoder.encode("manager"))
                    .build();
            List<UserInfo> userInfoList = List.of(user, userManager);
            userInfoRepository.saveAll(userInfoList);
//
//            List<String> authGroIds = List.of("ADMIN", "BAS_USER", "BEMS_USER");
//            List<AuthGroupUser> authGroupUsers = new ArrayList<>();
//
//            // for Manager
//            AuthGroupUser authGroupUserManager = new AuthGroupUser();
//            var authGroManager = authGroupRepository.findById("BEMS_USER");
//            authGroManager.ifPresent(authGroupUserManager::setAuthGroup);
//            authGroupUserManager.setUserInfo(userManager);
//            authGroupUserManager.setAuthGroupId("BEMS_USER");
//            authGroupUserManager.setUserId(user.getUserId());
//            authGroupUsers.add(authGroupUserManager);
//
//            // for Admin
//            for (String authGroId : authGroIds) {
//                AuthGroupUser authGroupUser = new AuthGroupUser();
//                var authGro = authGroupRepository.findById(authGroId);
//                authGro.ifPresent(authGroupUser::setAuthGroup);
//                authGroupUser.setUserInfo(user);
//                authGroupUser.setAuthGroupId(authGroId);
//                authGroupUser.setUserId(user.getUserId());
//                authGroupUsers.add(authGroupUser);
//            }
//            authGroupUserRepository.saveAll(authGroupUsers);
//
//            List<String> menu1s = List.of("NBSZ0000", "NBSZD000", "NBSZD010");
//            List<String> menu2s = List.of("NBSZ0000", "NBSZD000");
//            List<String> menu3s = List.of("NBSZD010");
//
//            List<AuthGroupMenu> authGroupMenus = new ArrayList<>();
//
//            for (String groupId : authGroIds) {
//                var authGroupOptional = authGroupRepository.findById(groupId);
//
//                // 그룹 ID에 따라 사용할 메뉴 리스트 결정
//                List<String> selectedMenus;
//                if ("ADMIN".equals(groupId)) {
//                    selectedMenus = menu1s;
//                } else if ("BAS_USER".equals(groupId)) {
//                    selectedMenus = menu2s;
//                } else if ("BEMS_USER".equals(groupId)) {
//                    selectedMenus = menu3s;
//                } else {
//                    continue; // 정의되지 않은 그룹 ID인 경우, 반복을 건너뜁니다.
//                }
//
//                // 선택된 메뉴 리스트에 대해 반복
//                for (String menuId : selectedMenus) {
//                    AuthGroupMenu authGroupMenu = new AuthGroupMenu();
//                    var menuOptional = menuRepository.findById(menuId);
//                    authGroupOptional.ifPresent(authGroupMenu::setAuthGroup);
//                    authGroupMenu.setMenuId(menuId);
//                    authGroupMenu.setAuthGroupId(groupId);
//                    menuOptional.ifPresent(authGroupMenu::setMenu);
//                    authGroupMenus.add(authGroupMenu);
//                }
//            }

// 나머지 코드 (예: authGroupMenus 저장)

//            authGroupMenuRepository.saveAll(authGroupMenus);

            //INSERT INTO tb_authgrp_user(authgroup_id,user_id,created_by,created_at,updated_by,updated_at) VALUES ('ADMIN','Admin','SYSTEM','2023-11-28 18:39:01.000','SYSTEM','2023-11-28 18:39:10.405')
            AuthGroupUser authGroupUser = AuthGroupUser.builder().authGroupId("ADMIN").userId("Admin").createdBy("SYSTEM").build();
            authGroupUserRepository.save(authGroupUser);

            var claims = JwtClaimsSet.builder()
                    .issuer("self") // 토큰 발행자
                    .issuedAt(Instant.now()) // 발행 시간
                    .expiresAt(Instant.now().plusMillis(860000000)) // 만료 시간
                    .subject("Admin") // 토큰 주체
                    .claim("scope", "ACCESS")
                    .build();

            System.out.println("Admin Token: "+jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());

        };
    }
}
