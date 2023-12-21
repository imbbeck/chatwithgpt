package com.nbs.nbs.entity.userInfo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthDTO {
    private String userId;
    private String userName;
    private String department;
    private String jobTitle; // jobTitleId 대신 jobTitle 사용
    private String jobTitleName; // 추가된 필드
}
