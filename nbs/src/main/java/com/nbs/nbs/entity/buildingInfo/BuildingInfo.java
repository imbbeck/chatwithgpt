package com.nbs.nbs.entity.buildingInfo;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_bldinfo")
public class BuildingInfo {

    @Id
    @Column(name = "bld_id", nullable = false, length = 4)
    private String buildingId;

    @Column(name = "bld_name", nullable = false, length = 50)
    private String buildingName;

    @Column(name = "bld_no", length = 20)
    private String facilityNumber;

    @Column(name = "bld_type", length = 10)
    private String facilityType;

    @Column(name = "bld_div", length = 10)
    private String buildingDivision;

    @Column(name = "bld_zipcode", length = 5)
    private String buildingZipcode;

    @Column(name = "bld_addr", length = 200)
    private String buildingAddress;

    @Column(name = "bld_addr2", length = 100)
    private String buildingAddressDetail;

    @Column(name = "bld_owner", length = 50)
    private String buildingOwner;

    @Temporal(TemporalType.DATE)
    @Column(name = "completion_date")
    private Date completionDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "warranty_date")
    private Date warrantyDate;

    @Column(name = "safetychk_yn", length = 1)
    private String safetyCheck;

    @Column(name = "maint_yn", length = 1)
    private String maintenance;

    @Temporal(TemporalType.DATE)
    @Column(name = "design_start")
    private Date designStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "design_end")
    private Date designEnd;

    @Column(name = "designer", length = 50)
    private String designer;

    @Temporal(TemporalType.DATE)
    @Column(name = "bld_start")
    private Date constructionStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "bld_end")
    private Date constructionEnd;

    @Column(name = "builder", length = 50)
    private String constructor;

    @Column(name = "totalcost")
    private BigDecimal totalCost;

    @Temporal(TemporalType.DATE)
    @Column(name = "supervise_start")
    private Date superviseStart;

    @Temporal(TemporalType.DATE)
    @Column(name = "supervise_end")
    private Date superviseEnd;

    @Column(name = "supervisor", length = 50)
    private String supervisor;

    @Column(name = "project_owner", length = 50)
    private String projectOwner;

    @Column(name = "project_name", length = 50)
    private String projectName;

    @Column(name = "inspector", length = 50)
    private String inspector;

    @Column(name = "photo_file1", length = 200)
    private String photoFile1;

    @Column(name = "photo_file2", length = 200)
    private String photoFile2;

    @Column(name = "remark", length = 1000)
    private String remark;

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 20)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false, length = 20)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors, getters, setters, etc.
}