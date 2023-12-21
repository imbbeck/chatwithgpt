package com.nbs.nbs.entity.commonCode.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveCommonCodeNameAndIdDTO {

    private String codeId;
    private String codeName;

}
