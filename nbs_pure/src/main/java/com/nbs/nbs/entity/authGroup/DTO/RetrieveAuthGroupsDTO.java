package com.nbs.nbs.entity.authGroup.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveAuthGroupsDTO {

    private String authGroupId;
    private String authGroupName;
    private String authGroupDesc;
    private String useYn;
}
