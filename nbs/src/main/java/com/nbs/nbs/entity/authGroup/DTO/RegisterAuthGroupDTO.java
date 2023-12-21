package com.nbs.nbs.entity.authGroup.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAuthGroupDTO {
    private String authGroupId;
    private String authGroupName;
    private String authGroupDesc;
    private String remark;
    private String useYn;
}
