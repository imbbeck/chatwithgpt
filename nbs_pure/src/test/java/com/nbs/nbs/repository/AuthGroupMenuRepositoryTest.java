package com.nbs.nbs.repository;

import com.nbs.nbs.entity.authGroup.AuthGroup;
import com.nbs.nbs.entity.authGroup.AuthGroupRepository;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenu;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenuId;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenuRepository;
import com.nbs.nbs.entity.menu.Menu;
import com.nbs.nbs.entity.menu.MenuRepository;
import com.nbs.nbs.entity.systemApplication.SystemApplication;
import com.nbs.nbs.entity.systemApplication.SystemApplicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthGroupMenuRepositoryTest extends BaseRepositoryTest<AuthGroupMenu, AuthGroupMenuId> {
    @Autowired
    private AuthGroupMenuRepository authGroupMenuRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private AuthGroupRepository authGroupRepository;

    @Autowired
    private SystemApplicationRepository systemApplicationRepository;

    @Override
    protected AuthGroupMenu createEntity() {
        String Id = stringId();
        SystemApplication systemApplication = SystemApplication.builder()
                .applicationId(Id)
                .applicationName("bems")
                .useYn("Y")
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")
                .build();
        systemApplicationRepository.save(systemApplication);
        Menu menu = Menu.builder()
                .menuId(Id)
                .menuName("app")
                .systemApplication(systemApplication)
                .useYn("Y")
                .sortOdr(1)
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")
                .build();
        menuRepository.save(menu);

        AuthGroup authGroup = AuthGroup.builder()
                .authGroupId(Id)
                .authGroupName(Id)
                .build();
        authGroupRepository.saveAndFlush(authGroup);

        AuthGroupMenu entity = AuthGroupMenu.builder()
                .authGroupId(Id)
                .menuId(Id)
                .menu(menu)
                .authGroup(authGroup)
                .build();
        return entity;
    }

    @Override
    protected AuthGroupMenuId getId(AuthGroupMenu entity) {
        AuthGroupMenuId authGroupMenuId = AuthGroupMenuId.builder()
                .authGroupId(entity.getAuthGroupId())
                .menuId(entity.getMenuId())
                .build();

        return authGroupMenuId;
    }

    // 추가 테스트 코드 작성
    // deleteByAuthGroupId 메소드 테스트, authGroupId를 이용해서 관련된 row 전부삭제
    @Transactional
    @Test
    public void testDeleteByAuthGroupId() {

        // authgroupmenu객체 생성
        AuthGroupMenu authGroupMenu1 = createEntity();
        AuthGroupMenu authGroupMenu2 = createEntity();
        authGroupMenu2.setAuthGroupId(authGroupMenu1.getAuthGroupId());

        // authGroupMenu3 다른 authGroupId사용
        AuthGroupMenu authGroupMenu3 = createEntity();

        // 저장
        authGroupMenuRepository.saveAll(List.of(authGroupMenu1,authGroupMenu2,authGroupMenu3));

        // 삭제전에 존재 여부 확인.
        boolean authGroupMenu1Exist = authGroupMenuRepository.existsById(getId(authGroupMenu1));
        boolean authGroupMenu2Exist = authGroupMenuRepository.existsById(getId(authGroupMenu2));
        boolean authGroupMenu3Exist = authGroupMenuRepository.existsById(getId(authGroupMenu3));

        //  authGroupMenu1의 authGroupId를 기준으로 삭제
        authGroupMenuRepository.deleteByAuthGroupId(authGroupMenu1.getAuthGroupId());

        // 삭제후에 존재 여부 확인.
        boolean authGroupMenu2ExistAfterDelte = authGroupMenuRepository.existsById(getId(authGroupMenu2));
        boolean authGroupMenu1ExistAfterDelte = authGroupMenuRepository.existsById(getId(authGroupMenu1));
        boolean authGroupMenu3ExistAfterDelte = authGroupMenuRepository.existsById(getId(authGroupMenu3));

        // 삭제전에 존재 확인
        assertTrue(authGroupMenu1Exist);
        assertTrue(authGroupMenu2Exist);
        assertTrue(authGroupMenu3Exist);

        // 삭제후에 존재 확인
        assertFalse(authGroupMenu2ExistAfterDelte);
        assertFalse(authGroupMenu1ExistAfterDelte);
        assertTrue(authGroupMenu3ExistAfterDelte);

    }


}
