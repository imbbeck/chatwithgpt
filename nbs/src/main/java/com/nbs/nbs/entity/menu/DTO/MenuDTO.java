package com.nbs.nbs.entity.menu.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
    private String menuId;
    private String menuName;
    private int sortOdr;
    private String useYn;

    // 나머지 필드...
    private Set<MenuDTO> childMenus;

}
