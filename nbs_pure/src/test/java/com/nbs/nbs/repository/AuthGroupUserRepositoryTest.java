package com.nbs.nbs.repository;

import com.nbs.nbs.entity.authGroup.AuthGroup;
import com.nbs.nbs.entity.authGroup.AuthGroupRepository;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenu;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenuRepository;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUser;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUserId;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUserRepository;
import com.nbs.nbs.entity.menu.Menu;
import com.nbs.nbs.entity.menu.MenuRepository;
import com.nbs.nbs.entity.systemApplication.SystemApplication;
import com.nbs.nbs.entity.systemApplication.SystemApplicationRepository;
import com.nbs.nbs.entity.userInfo.UserInfo;
import com.nbs.nbs.entity.userInfo.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthGroupUserRepositoryTest extends BaseRepositoryTest<AuthGroupUser, AuthGroupUserId> {
    @Autowired
    private AuthGroupUserRepository authGroupUserRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AuthGroupRepository authGroupRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    AuthGroupMenuRepository authGroupMenuRepository;

    @Autowired
    SystemApplicationRepository systemApplicationRepository;





    @Override
    protected AuthGroupUser createEntity() {
        String Id = stringId();

        UserInfo userInfo = UserInfo.builder()
                .userId(Id)
                .userPwd(passwordEncoder.encode("asd"))
                .jobTitle("A010001")
                .build();
        userInfoRepository.saveAndFlush(userInfo);

        AuthGroup authGroup = AuthGroup.builder()
                .authGroupId(Id)
                .authGroupName(Id)
                .useYn("Y")
                .build();
        authGroupRepository.saveAndFlush(authGroup);

        AuthGroupUser entity = AuthGroupUser.builder()
                .userId(Id)
                .authGroupId(Id)
                .userInfo(userInfo)
                .authGroup(authGroup)
                .build();
        return entity;
    }

    @Override
    protected AuthGroupUserId getId(AuthGroupUser entity) {
        AuthGroupUserId authGroupUserId = AuthGroupUserId.builder()
                .userId(entity.getUserId())
                .authGroupId(entity.getAuthGroupId())
                .build();
        return authGroupUserId;
    }

    // 추가 테스트 코드

    // findMenuIdsByUserId 메소드 테스트, UserId로 MenuId list 가져오는 것 테스트
    @Transactional
    @Test
    public void testFindMenuIdsByUserId() {
        String id = stringId();

        // authgroupuser 객체 생성
        AuthGroupUser authGroupUser = createEntity();

        // save authgroupuser
        authGroupUserRepository.save(authGroupUser);

        // systemapplication 생성
        SystemApplication systemApplication = SystemApplication.builder()
                .applicationId(id)
                .applicationName("bems")
                .useYn("Y")
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")
                .build();
        systemApplicationRepository.save(systemApplication);

        // menu 생성
        Menu menu = Menu.builder()
                .menuId(id)
                .menuName("app")
                .prntmenuId(id)
                .useYn("Y")
                .systemApplication(systemApplication)
                .sortOdr(1)
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")
                .build();

        // menu 저장
        menuRepository.save(menu);

        // authgroupmenu 생성
        AuthGroupMenu authGroupMenu = AuthGroupMenu.builder()
                .authGroup(authGroupUser.getAuthGroup())
                .menu(menu)
                .menuId(menu.getMenuId())
                .authGroupId(authGroupUser.getAuthGroupId())
                .build();

        // authgroupmenu 저장
        authGroupMenuRepository.save(authGroupMenu);

        // userId 이용해서 menuIds 가져오기
        List<String> menuIds = authGroupUserRepository.findMenuIdsByUserId(authGroupUser.getUserId());

        // 가져온 menuId가 연결한 메뉴랑 같은지 확인하는 변수 선언
        boolean menuIdsAllmatches = menuIds.stream().allMatch(str -> str.equals(menu.getMenuId()));

        // 검수
        assertTrue(menuIdsAllmatches);
    }

    // findAuthGroupIdsByUserId 메소드 테스트, // userId를 기반으로 모든 관련된 authGroupId 가져오는지 테스트
    @Transactional
    @Test
    public void testFindAuthGroupIdsByUserId() {

        // authgroupuser 객체 생성
        AuthGroupUser authGroupUser = createEntity();

        // authgroupuser 저장
        authGroupUserRepository.save(authGroupUser);

        // authGroupId 가져오기
        List<String> authGroupIds = authGroupUserRepository.findAuthGroupIdsByUserId(authGroupUser.getUserId());

        // authGroupIdsAllMatches
        boolean authGroupIdsAllMatches = authGroupIds.stream().allMatch(str -> str.equals(authGroupUser.getAuthGroupId()));

        // 검수
        assertTrue(authGroupIdsAllMatches);
    }

    // deleteByAuthGroupId 메소드 테스트, // 특정 augroupId를 가지는 모든 객체 삭제
    @Transactional
    @Test
    public void testDeleteByAuthGroupId() {

        // authgroupuser 객체 생성
        AuthGroupUser authGroupUser = createEntity();

        // authgroupuser 저장
        authGroupUserRepository.save(authGroupUser);

        // 삭제전 저장확인
        AuthGroupUserId authGroupUserId = getId(authGroupUser);
        boolean authGroupUserExistBeforeDelete = authGroupUserRepository.existsById(authGroupUserId);

        // 삭제
        authGroupUserRepository.deleteByAuthGroupId(authGroupUser.getAuthGroupId());
        boolean authGroupUserExistAfterDelete = authGroupUserRepository.existsById(authGroupUserId);

        // 삭제전 존재여부 확인
        assertTrue(authGroupUserExistBeforeDelete);
        // 삭제후 존재여부 확인
        assertFalse(authGroupUserExistAfterDelete);
    }



}
