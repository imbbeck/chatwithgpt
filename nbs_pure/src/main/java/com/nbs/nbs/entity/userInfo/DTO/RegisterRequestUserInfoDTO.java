package com.nbs.nbs.entity.userInfo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestUserInfoDTO {

    private String userId;
    private String userName;
    private String email;
    private String resetPwd;
    private String userPwd;
    private String department;
    private String jobTitle;
    private String cellPhone;
    private String telPhone;
    private String remark;
    private String useYn;
    private String delYn;
}
