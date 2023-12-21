package com.nbs.nbs.entity.authGroupUser;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthGroupUserId implements Serializable {

    @Column(name = "authgroup_id")
    private String authGroupId;

    @Column(name = "user_id")
    private String userId;

    // 생성자, 게터, 세터, equals, hashCode...
}
