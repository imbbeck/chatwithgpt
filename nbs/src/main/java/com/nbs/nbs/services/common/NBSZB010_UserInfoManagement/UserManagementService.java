package com.nbs.nbs.services.common.NBSZB010_UserInfoManagement;

import com.nbs.nbs.entity.commonCode.CommonCodeRepository;
import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeNameAndIdDTO;
import com.nbs.nbs.entity.userInfo.DTO.RegisterRequestUserInfoDTO;
import com.nbs.nbs.entity.userInfo.DTO.RetrieveOneUserInfoDetails;
import com.nbs.nbs.entity.userInfo.UserInfo;
import com.nbs.nbs.entity.userInfo.UserInfoRepository;
import com.nbs.nbs.exception.DataNotFoundException;
import com.nbs.nbs.exception.IdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommonCodeRepository commonCodeRepository;
    private final String jobTitleCodeId = "A010000";

    public List<RetrieveOneUserInfoDetails> retrieveAllUsers() {
        return userInfoRepository.findDTOUsers();
    }

    public RetrieveOneUserInfoDetails retrieveOneUser(String userId) {
        return userInfoRepository.findDetailsByUserId(userId).orElseThrow(() -> new DataNotFoundException("유저 정보가 없습니다."));
    }

    public void updateUser(String userId, RegisterRequestUserInfoDTO request) {
        var existingUser = userInfoRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        BeanUtils.copyProperties(request, existingUser, "userId", "userPwd", "resetPwd");
        existingUser.setUserPwd(passwordEncoder.encode(request.getUserPwd()));
        existingUser.setResetPwd(passwordEncoder.encode(request.getResetPwd()));
        userInfoRepository.save(existingUser);
    }

    public void registerUser(RegisterRequestUserInfoDTO request) {
        isPresentUser(request.getUserId());
        UserInfo user = new UserInfo();
        BeanUtils.copyProperties(request, user, "userPwd", "resetPwd");
        user.setUserPwd(passwordEncoder.encode(request.getUserPwd()));
        user.setResetPwd(passwordEncoder.encode(request.getResetPwd()));
        userInfoRepository.save(user);
    }

    public void resetPassword(String userId) {
        var user = userInfoRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("유저가 존재하지 않습니다."));
        user.setUserPwd(passwordEncoder.encode(user.getResetPwd()));
        userInfoRepository.save(user);
    }

    private void isPresentUser(String userId) {
        boolean presentUser = userInfoRepository.existsById(userId);
        if (presentUser) {
            throw new IdAlreadyExistsException("이미 존재하는 ID입니다");
        }
    }

    public List<RetrieveCommonCodeNameAndIdDTO> retrieveJobTitle() {
        return commonCodeRepository.findCodeNamesByPrntcodeId(jobTitleCodeId);
    }

    public void deleteUser(String userId) {
        userInfoRepository.deleteById(userId);
    }

    public List<RetrieveCommonCodeNameAndIdDTO> retrieveAllSubCommonCode(String commonCodeId) {
        return commonCodeRepository.findCodeNamesByPrntcodeId(commonCodeId);
    }
}
