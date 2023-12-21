package com.nbs.nbs.services.common.NBSZD030_AuthGroupMenuManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAuthGroupIdMenuIdRequest {

    @JsonProperty("menuIds")
    private List<String> menuIds;
}
