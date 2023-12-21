package com.nbs.nbs.entity.commonCode.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RetrieveCommonCodeDTO {

    private String codeId;
    private String codeName;
    private String prntcodeId;
    private int sortOdr;
    private String useYn;
}
