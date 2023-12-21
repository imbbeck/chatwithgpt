package com.nbs.nbs.entity.menu.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMenuDTO {

    private String applicationName;
    private String menuId;
    private String menuName;
    private int sortOdr;
    private String prntmenuId;
    private String menuUrl;
    private String remark;
    private String useYn;

}
