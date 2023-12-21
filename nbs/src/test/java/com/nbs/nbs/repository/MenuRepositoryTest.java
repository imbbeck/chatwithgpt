package com.nbs.nbs.repository;

import com.nbs.nbs.entity.authGroup.AuthGroup;
import com.nbs.nbs.entity.authGroup.AuthGroupRepository;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenu;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenuRepository;
import com.nbs.nbs.entity.menu.DTO.MenuAuthGroupDTO;
import com.nbs.nbs.entity.menu.DTO.RetrieveMenuDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MenuRepositoryTest extends BaseRepositoryTest<Menu, String> {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private SystemApplicationRepository systemApplicationRepository;
    @Autowired
    private AuthGroupMenuRepository authGroupMenuRepository;
    @Autowired
    private AuthGroupRepository authGroupRepository;

    @Override
    protected Menu createEntity() {
        String id = stringId();
        String prntId = stringId();

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

        Menu entity = Menu.builder()
                .menuId(id)
                .menuName("app")
                .prntmenuId(prntId)
                .systemApplication(systemApplication)
                .useYn("Y")
                .sortOdr(1)
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")
                .build();
        return entity;
    }

    @Override
    protected String getId(Menu entity) {
        return entity.getMenuId();
    }

    // prntmenuId가 변수랑 같은 Menu가 있는지 조회하는 기능 테스트
    @Transactional
    @Test
    public void testExistsByPrntmenuId() {
        Menu targetMenu = createEntity();
        String menuPrntId = targetMenu.getPrntmenuId();
        boolean beforeSaveExistsByPrntmenuId = menuRepository.existsByPrntmenuId(menuPrntId);
        menuRepository.save(targetMenu);
        boolean AfterSaveExistsByPrntmenuId = menuRepository.existsByPrntmenuId(menuPrntId);
        // 검증
        assertFalse(beforeSaveExistsByPrntmenuId);
        assertTrue(AfterSaveExistsByPrntmenuId);
    }

    // menuId로 RetrieveMenuDTO형식으로 List 제공하는 기능 테스트
    @Transactional
    @Test
    public void testFindDTOMenuByMenuId() {
        Menu targetMenu = createEntity();
        String menuId = targetMenu.getMenuId();
        menuRepository.save(targetMenu);
        var menuDTO = menuRepository.findDTOMenuByMenuId(menuId);
        boolean menuDTOExist = menuDTO.isPresent();
        var menuOne = menuRepository.findById(menuId);
        boolean menuOneExist = menuOne.isPresent();

// 검증
        assertTrue(menuDTOExist);
        assertTrue(menuOneExist);
        assertThat(menuOne.get().getSystemApplication().getApplicationName()).isEqualTo(menuDTO.get().getApplicationName());
    }

    // systemId를 통해서 해당하는 Menu를 조회하는기능 테스트
    @Transactional
    @Test
    public void testFindDTOAllMenu() {
        // systemID
        String bas = "BAS";
        String bems = "BEMS";
        String cmn = "CMN";
        // systemId가 각각 다른 menu 생성
        Menu targetMenuBEMS = createEntity();
        SystemApplication systemApplicationBems = targetMenuBEMS.getSystemApplication();
        systemApplicationBems.setApplicationName(bems);

        Menu targetMenuBAS = createEntity();
        SystemApplication systemApplicationBAS = targetMenuBAS.getSystemApplication();
        systemApplicationBAS.setApplicationName(bas);

        Menu targetMenuCMN = createEntity();
        SystemApplication systemApplicationCMN = targetMenuCMN.getSystemApplication();
        systemApplicationCMN.setApplicationName(cmn);

        systemApplicationRepository.saveAll(List.of(systemApplicationBems,systemApplicationCMN,systemApplicationBAS));
        menuRepository.saveAll(List.of(targetMenuBEMS,targetMenuBAS,targetMenuCMN));

        List<RetrieveMenuDTO> bemsList = menuRepository.findDTOAllMenu(systemApplicationBems.getApplicationId());
        List<RetrieveMenuDTO> basList = menuRepository.findDTOAllMenu(systemApplicationBAS.getApplicationId());
        List<RetrieveMenuDTO> cmnList = menuRepository.findDTOAllMenu(systemApplicationCMN.getApplicationId());

        // 공통 코드는 디폴트로 조회가 된다.
        boolean allBems = bemsList.stream()
                .allMatch(dto -> dto.getApplicationName().equals(bems) || dto.getApplicationName().equals("공통") );

        boolean allBAS = basList.stream()
                .allMatch(dto -> dto.getApplicationName().equals(bas) || dto.getApplicationName().equals("공통") );

        boolean allCMN = cmnList.stream()
                .allMatch(dto -> dto.getApplicationName().equals(cmn) || dto.getApplicationName().equals("공통") );

        // 검증
        assertTrue(allBems);
        assertTrue(allBAS);
        assertTrue(allCMN);
    }

    // authgroupId랑 systemId로 해당 권한이 가진 메뉴를가져옵니다.
    // 예를들면 ADMIN에 특정 BEMS 관련 메뉴를 연결하면, ADMIN, BEMS 를 이용해서 사용가능 메뉴를 가져옴
    @Transactional
    @Test
    public void testFindDTOMenusByAuthGroupIdAndSystemId() {

        // menu 생성
        Menu menu = createEntity();
        String id = menu.getMenuId();

        // Authgroup 생성
        AuthGroup authGroup = AuthGroup.builder()
                .authGroupId(id)
                .authGroupName(id)
                .useYn("Y")
                .build();

        menuRepository.save(menu);
        authGroupRepository.save(authGroup);

        AuthGroupMenu authGroupMenu = AuthGroupMenu.builder()
                .authGroupId(authGroup.getAuthGroupId())
                        .menuId(menu.getMenuId())
                        .menu(menu)
                        .authGroup(authGroup)
                        .build();
        authGroupMenuRepository.save(authGroupMenu);

        List<MenuAuthGroupDTO> result = menuRepository.findDTOMenusByAuthGroupIdAndSystemId(authGroup.getAuthGroupId(),menu.getSystemApplication().getApplicationId());
        boolean resultExist = !result.isEmpty();
        boolean resultAllMatch = result.stream().allMatch(dto -> dto.getAuthGroupId().equals(authGroup.getAuthGroupId()) && dto.getMenuId().equals(menu.getMenuId()));

// 검증
        assertTrue(resultExist);
        assertTrue(resultAllMatch);
    }
}
