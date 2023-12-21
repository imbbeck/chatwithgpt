package com.nbs.nbs.services.common.NBSZD030_AuthGroupMenuManagement;

import com.nbs.nbs.entity.authGroup.AuthGroupRepository;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupsDTO;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenu;
import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenuRepository;
import com.nbs.nbs.entity.menu.DTO.MenuAuthGroupDTO;
import com.nbs.nbs.entity.menu.MenuRepository;
import com.nbs.nbs.entity.systemApplication.DTO.RetrieveSystemApplicationNameDTO;
import com.nbs.nbs.entity.systemApplication.SystemApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthGroupMenuManagementService {
    private final AuthGroupRepository authGroupRepository;
    private final MenuRepository menuRepository;
    private final SystemApplicationRepository systemApplicationRepository;
    private final AuthGroupMenuRepository authGroupMenuRepository;

    // auth group 리스트
    public List<RetrieveAuthGroupsDTO> retrieveAllAuthGroups() {
        return authGroupRepository.findDTOAll();
    }

    public List<MenuAuthGroupDTO> retrieveAllUsersByAuthGroupId(String groupId, String systemId) {
        return menuRepository.findDTOMenusByAuthGroupIdAndSystemId(groupId,systemId);
    }

    public List<RetrieveSystemApplicationNameDTO> retrieveAllSystemApplicationNameByUseYn() {
        return systemApplicationRepository.findDTOByUseYn();
    }

    public void addAuthGroupIdMenuId(String authGroupId, AddAuthGroupIdMenuIdRequest request) {
        // 해당 authGroupId에 대한 모든 레코드 삭제
        authGroupMenuRepository.deleteByAuthGroupId(authGroupId);

        // 새로운 메뉴 목록을 저장
        List<AuthGroupMenu> menusToSave = request.getMenuIds().stream()
                .map(menuId -> AuthGroupMenu.builder()
                        .authGroupId(authGroupId)
                        .menuId(menuId)
                        .build())
                .collect(Collectors.toList());

        authGroupMenuRepository.saveAll(menusToSave);
    }
}
