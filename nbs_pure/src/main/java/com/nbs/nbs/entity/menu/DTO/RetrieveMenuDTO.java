package com.nbs.nbs.entity.menu.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveMenuDTO {
    private String applicationName;
    private String menuId;
    private String menuName;
    private int sortOdr;
    private String prntmenuId;
    private String menuUrl;
    private String remark;
    private String useYn;
    private String useYnName;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    public String getUseYnName() {
        if ("Y".equals(this.useYnName.toUpperCase())) {
            return "사용함";
        } else if ("N".equals(this.useYnName.toUpperCase())) {
            return "사용안함";
        }
        return this.useYnName; // 혹은 null 또는 다른 기본값을 반환할 수 있음
    }
}
