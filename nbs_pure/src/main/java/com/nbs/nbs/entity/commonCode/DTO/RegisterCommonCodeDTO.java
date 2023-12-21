package com.nbs.nbs.entity.commonCode.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCommonCodeDTO {
    private String codeId;
    private String prntcodeId ;
    private String codeName;
    private String codeDesc;
    private int sortOdr;
    private String useYn;
    private String remark;
    private String sprfield1;
    private String sprfield2;
    private String sprfield3;
    private String sprfield4;
    private String sprfield5;

}
