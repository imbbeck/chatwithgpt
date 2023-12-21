package com.nbs.nbs.services.common.NBSZD020_AuthGroupUserManagement;

import com.nbs.nbs.entity.authGroup.AuthGroupRepository;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupsDTO;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUser;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUserId;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUserRepository;
import com.nbs.nbs.entity.userInfo.DTO.UserAuthDTO;
import com.nbs.nbs.entity.userInfo.UserInfoRepository;
import com.nbs.nbs.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthGroupUserManagementService {
    private final AuthGroupRepository authGroupRepository;
    private final UserInfoRepository userInfoRepository;
    private final AuthGroupUserRepository authGroupUserRepository;

    // auth group 리스트
    public List<RetrieveAuthGroupsDTO> retrieveAuthGroups() {
        return authGroupRepository.findDTOAll();
    }

    public RetrieveAuthGroupUserResponse retrieveUsersByAuthGroupId(String groupId) {
        List<UserAuthDTO> userInGroup = userInfoRepository.findDTOUsersByAuthGroupId(groupId);
        List<UserAuthDTO> userNotInGroup = userInfoRepository.findDTOUsersByAuthGroupIdWhereNotIn(groupId);

        return RetrieveAuthGroupUserResponse.builder()
                .usersInGroup(userInGroup)
                .usersNotInGroup(userNotInGroup)
                .build();
    }

    public void addDeleteAuthGroupIdUserId(String authGroupId,AddDeleteAuthGroupIdUserIdRequest request) {
        authGroupUserRepository.deleteByAuthGroupId(authGroupId);
        // 새로운 사용자 목록을 저장
        List<AuthGroupUser> usersToSave = request.getUsersInGroup().stream()
                .map(userId -> AuthGroupUser.builder()
                        .authGroupId(authGroupId)
                        .userId(userId)
                        .build())
                .collect(Collectors.toList());
        authGroupUserRepository.saveAll(usersToSave);

    }
}
