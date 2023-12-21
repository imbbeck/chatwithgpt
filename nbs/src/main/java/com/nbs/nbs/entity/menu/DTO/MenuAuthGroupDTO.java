package com.nbs.nbs.entity.menu.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuAuthGroupDTO {
    String menuId;
    String menuName;
    String authGroupId;
}
