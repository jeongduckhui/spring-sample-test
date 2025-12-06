package com.example.demo.domain.common.code;

import lombok.Data;

@Data
public class CommonCode {
    private Long id;
    private String groupCode;
    private String codeValue;
//    private String codeName;
    private Integer sortSeq;
    private String useYn;
}