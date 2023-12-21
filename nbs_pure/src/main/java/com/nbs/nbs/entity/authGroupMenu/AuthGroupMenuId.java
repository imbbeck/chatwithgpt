package com.nbs.nbs.entity.authGroupMenu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthGroupMenuId implements Serializable {
    private String authGroupId;
    private String menuId;
}
