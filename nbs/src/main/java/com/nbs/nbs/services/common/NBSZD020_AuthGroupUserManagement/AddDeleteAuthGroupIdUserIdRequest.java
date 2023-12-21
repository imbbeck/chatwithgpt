package com.nbs.nbs.services.common.NBSZD020_AuthGroupUserManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddDeleteAuthGroupIdUserIdRequest {

    @JsonProperty("userIdsInGroup")
    private List<String> usersInGroup;
//    @JsonProperty("userIDssNotInGroup")
//    private List<String> usersNotInGroup;
}
