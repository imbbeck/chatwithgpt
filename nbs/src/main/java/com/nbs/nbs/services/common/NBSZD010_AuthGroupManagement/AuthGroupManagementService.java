package com.nbs.nbs.services.common.NBSZD010_AuthGroupManagement;

import com.nbs.nbs.entity.authGroup.AuthGroup;
import com.nbs.nbs.entity.authGroup.AuthGroupRepository;
import com.nbs.nbs.entity.authGroup.DTO.RegisterAuthGroupDTO;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupDTO;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupsDTO;
import com.nbs.nbs.exception.DataNotFoundException;
import com.nbs.nbs.exception.IdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthGroupManagementService {
    private final AuthGroupRepository authGroupRepository;

    public List<RetrieveAuthGroupsDTO> retrieveAllAuthGroups() {
        return authGroupRepository.findDTOAll();
    }

    public RetrieveAuthGroupDTO retrieveOneAuthGroup(String authgroupId) {
        return authGroupRepository.findDTOAuthGroupById(authgroupId).orElseThrow(() -> new DataNotFoundException("권한그룹이 없습니다."));
    }

    public void updateAuthGroup(String authgroupId, RegisterAuthGroupDTO request) {
        var exsistingAuthGroup = authGroupRepository.findById(authgroupId)
                .orElseThrow(() -> new DataNotFoundException("권한그룹이 없습니다."));
        BeanUtils.copyProperties(request, exsistingAuthGroup, "authGroupId");
        authGroupRepository.save(exsistingAuthGroup);
    }

    public void createAUthGroup(RegisterAuthGroupDTO request) {
        isPresent(request.getAuthGroupId());
        AuthGroup authGroup = new AuthGroup();
        BeanUtils.copyProperties(request, authGroup);
        authGroupRepository.save(authGroup);
    }

    private void isPresent(String authGroupId) {
        boolean existingAuthGroup = authGroupRepository.existsById(authGroupId);
        if (existingAuthGroup) {
            throw new IdAlreadyExistsException("이미 존재하는 ID입니다.");
        }
    }

    public void deleteAuthGroup(String authgroupId) {
        authGroupRepository.deleteById(authgroupId);
    }
}
