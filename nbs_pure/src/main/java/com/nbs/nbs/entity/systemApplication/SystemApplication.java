package com.nbs.nbs.entity.systemApplication;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "tb_systemapp")
public class SystemApplication {

    @Id
    @Column(name = "application_id")
    private String applicationId;

    @Column(name = "application_name", nullable = false)
    private String applicationName;

    @Column(name = "application_desc")
    private String applicationDesc;

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

    @Builder.Default
    @Column(name = "use_yn", nullable = false)
    private String useYn = "Y";

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Getters and Setters
}
