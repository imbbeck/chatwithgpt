package com.nbs.nbs.entity.buildingInfo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBuildingDTO {
    private String buildingId;
    private String buildingName;
    private String facilityNumber;
    private String facilityType;
    private String buildingDivision;
    private String buildingZipcode;
    private String buildingAddress;
    private String buildingAddressDetail;
    private String buildingOwner;
    private Date completionDate;
    private Date warrantyDate;
    private String safetyCheck;
    private String maintenance;
    private Date designStart;
    private Date designEnd;
    private String designer;
    private Date constructionStart;
    private Date constructionEnd;
    private String constructor;
    private BigDecimal totalCost;
    private Date superviseStart;
    private Date superviseEnd;
    private String supervisor;
    private String projectOwner;
    private String projectName;
    private String inspector;
    private String photoFile1;
    private String photoFile2;
    private String remark;

}
