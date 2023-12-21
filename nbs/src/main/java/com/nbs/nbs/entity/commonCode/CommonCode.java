package com.nbs.nbs.entity.commonCode;

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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@IdClass(CommonCodeId.class)
@Table(name = "tb_commoncode")
public class CommonCode {

    @Id
    @Column(name = "code_id", nullable = false)
    private String codeId;

    @Id
    @Builder.Default
    @Column(name = "prntcode_id")
    private String prntcodeId = "0000000000";

    @Column(name = "code_name", nullable = false)
    private String codeName;

    @Column(name = "code_desc")
    private String codeDesc;

    @Column(name = "sort_odr", nullable = false)
    private int sortOdr;

    @Column(name = "use_yn", nullable = false)
    private String useYn;

    @Column(name = "remark")
    private String remark;

    @Column(name = "sprfield1")
    private String sprfield1;

    @Column(name = "sprfield2")
    private String sprfield2;

    @Column(name = "sprfield3")
    private String sprfield3;

    @Column(name = "sprfield4")
    private String sprfield4;

    @Column(name = "sprfield5")
    private String sprfield5;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters
}
