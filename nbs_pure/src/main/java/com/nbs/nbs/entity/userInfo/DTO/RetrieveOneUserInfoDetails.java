package com.nbs.nbs.entity.userInfo.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RetrieveOneUserInfoDetails {
    private String userId;
    private String userName;
    private String department;
    private String jobTitle; // jobTitleId 대신 jobTitle 사용
    private String jobTitleName; // 추가된 필드
    private String cellPhone;
    private String telPhone;
    private String email;
    private String remark;
    private String useYn;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}