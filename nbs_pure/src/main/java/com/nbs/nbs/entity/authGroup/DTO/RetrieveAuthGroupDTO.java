package com.nbs.nbs.entity.authGroup.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveAuthGroupDTO {
    private String authGroupId;
    private String authGroupName;
    private String authGroupDesc;
    private String remark;
    private String useYn;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
}
