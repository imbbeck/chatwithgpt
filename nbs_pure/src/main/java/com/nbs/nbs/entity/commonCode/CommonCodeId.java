package com.nbs.nbs.entity.commonCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonCodeId implements Serializable {
    private String codeId;
    private String prntcodeId;

    // 기본 생성자, getter, setter, equals, hashCode 메소드
}
