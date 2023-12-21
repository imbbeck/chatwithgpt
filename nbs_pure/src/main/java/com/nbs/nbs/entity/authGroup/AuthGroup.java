package com.nbs.nbs.entity.authGroup;

import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenu;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_authgroup")
public class AuthGroup {

    @Id
    @Column(name = "authgroup_id", nullable = false, length = 20) // 그룹 ID
    private String authGroupId;

    @Column(name = "authgroup_name", nullable = false, length = 50) // 그룹 이름
    private String authGroupName;

    @Column(name = "authgroup_desc", length = 300) // 그룹 설명
    private String authGroupDesc;

    @Column(name = "remark", length = 1000) // 비고
    private String remark;

    @Builder.Default
    @Column(name = "use_yn", nullable = false, length = 1) // 사용 여부
    private String useYn = "Y";

    @CreatedBy
    @Column(name = "created_by", length = 20) // 등록자
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at") // 등록일시
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by", length = 20) // 수정자
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at") // 수정일시
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "authGroup", fetch = FetchType.LAZY)
    private Set<AuthGroupMenu> authGroupMenus;

    @OneToMany(mappedBy = "authGroup", fetch = FetchType.LAZY)
    private Set<AuthGroupUser> authGroupUsers;

    // Getter와 Setter
}
