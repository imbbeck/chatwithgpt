package com.nbs.nbs.services.common.NBSZD020_AuthGroupUserManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nbs.nbs.entity.userInfo.DTO.UserAuthDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveAuthGroupUserResponse {

    @JsonProperty("usersInGroup")
    private List<UserAuthDTO> usersInGroup;

    @JsonProperty("usersNotInGroup")
    private List<UserAuthDTO> usersNotInGroup;

}
