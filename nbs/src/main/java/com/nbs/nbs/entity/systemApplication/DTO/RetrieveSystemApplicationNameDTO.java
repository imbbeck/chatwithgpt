package com.nbs.nbs.entity.systemApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveSystemApplicationNameDTO {
    private String applicationId;
    private String applicationName;
}
