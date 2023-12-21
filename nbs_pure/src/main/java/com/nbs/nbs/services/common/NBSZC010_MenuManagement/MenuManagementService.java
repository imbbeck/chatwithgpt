package com.nbs.nbs.services.common.NBSZC010_MenuManagement;

import com.nbs.nbs.entity.menu.DTO.RegisterMenuDTO;
import com.nbs.nbs.entity.menu.DTO.RetrieveMenuDTO;
import com.nbs.nbs.entity.menu.Menu;
import com.nbs.nbs.entity.menu.MenuRepository;
import com.nbs.nbs.entity.systemApplication.SystemApplication;
import com.nbs.nbs.entity.systemApplication.SystemApplicationRepository;
import com.nbs.nbs.exception.DataNotFoundException;
import com.nbs.nbs.exception.IdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuManagementService {
    private final MenuRepository menuRepository;

    private final SystemApplicationRepository systemApplicationRepository;
    private final String superMenuPrntmenuId = "NBS00000";
    private final String typeSub = "sublevel";
    private final String typeSame = "samelevel";


//    public List<MenuDTO> retrieveMenus() {
//        List<Menu> menus = menuRepository.findAll();
//        if (menus.isEmpty()) {
//            throw new DataNotFoundException("메뉴가 존재하지 않습니다.");
//        }
//        Map<String, MenuDTO> dtoMap = new HashMap<>();
//        for (Menu menu : menus) {
//            MenuDTO dto = new MenuDTO();
//            BeanUtils.copyProperties(menu, dto);
//            dto.setChildMenus(new HashSet<>());
//            dtoMap.put(menu.getMenuId(), dto);
//        }
//
//        List<MenuDTO> rootMenus = new ArrayList<>();
//        for (Menu menu : menus) {
//            MenuDTO dto = dtoMap.get(menu.getMenuId());
//            if (Objects.equals(menu.getPrntmenuId(), superMenuPrntmenuId)) {
//                rootMenus.add(dto);
//            } else {
//                MenuDTO parentDto = dtoMap.get(menu.getPrntmenuId());
//                if (parentDto != null) {
//                    parentDto.getChildMenus().add(dto);
//                }
//            }
//        }
//        return rootMenus;
//    }

    public List<RetrieveMenuDTO> retrieveAllMenus(String systemId) {
        return menuRepository.findDTOAllMenu(systemId);
    }

    public RetrieveMenuDTO retrieveOneMenu(String menuId) {
        return menuRepository.findDTOMenuByMenuId(menuId).orElseThrow(() -> new DataNotFoundException("메뉴가 존재하지 않습니다."));
    }


    private void isPresent(String menuId) {
        boolean existingMenu = menuRepository.existsById(menuId);
        if (existingMenu) {
            throw new IdAlreadyExistsException("이미 존재하는 ID입니다.");
        }
    }

    public void createMenu(String menuId, String prntmenuId, String type, RegisterMenuDTO request) {
        if (!type.equals(typeSub) && !type.equals(typeSame)) {
            throw new RuntimeException("올바른 type을 입력하세요");
        }
        // bas에 대한 메뉴추가 만 허용
        if (!"BAS".equals(request.getApplicationName())) {
            throw new RuntimeException("시스템코드 'BAS'만 허용됩니다.");
        }
        SystemApplication systemApplication = systemApplicationRepository.findById("BAS").orElseThrow(() -> new DataNotFoundException("시스템코드  'BAS' 없습니다. "));
        isPresent(request.getMenuId());
        Menu menu = new Menu();
        if (type.equals(typeSame)) {
            BeanUtils.copyProperties(request, menu, "prntmenuId");
            menu.setPrntmenuId(prntmenuId);
            menu.setSystemApplication(systemApplication);
        }
        if (type.equals(typeSub)) {
            BeanUtils.copyProperties(request, menu, "prntmenuId");
            menu.setPrntmenuId(menuId);
            menu.setSystemApplication(systemApplication);
        }
        if (menu.getMenuId() != null) {
            menuRepository.save(menu);
        }

    }

    public void updateMenu(String menuId, RegisterMenuDTO request) {
        var memu = menuRepository.findById(menuId).orElseThrow(() -> new DataNotFoundException("해당 메뉴가 존재하지 않습니다"));
        BeanUtils.copyProperties(request, memu, "menuId", "applicationName", "prntmenuId");
        menuRepository.save(memu);
    }

    public void deleteMenu(String menuId) {
        boolean isPresentSubMenu = menuRepository.existsByPrntmenuId(menuId);
        if (isPresentSubMenu) {
            throw new RuntimeException("메뉴 대한 하위 메뉴가 존재 합니다.");
        }
        menuRepository.deleteById(menuId);
    }

    public void createSuperMenu(RegisterMenuDTO request) {

        // bas에 대한 메뉴추가 만 허용
        if (!"BAS".equals(request.getApplicationName())) {
            throw new RuntimeException("시스템코드 'BAS'만 허용됩니다.");
        }
        SystemApplication systemApplication = systemApplicationRepository.findById("BAS").orElseThrow(() -> new DataNotFoundException("시스템코드  'BAS' 없습니다. "));
        isPresent(request.getMenuId());
        Menu menu = new Menu();
        BeanUtils.copyProperties(request, menu, "prntmenuId");
        menu.setPrntmenuId(superMenuPrntmenuId);
        menu.setSystemApplication(systemApplication);
        menuRepository.save(menu);
    }
}
