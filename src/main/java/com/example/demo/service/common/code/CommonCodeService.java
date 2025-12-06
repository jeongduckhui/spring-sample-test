package com.example.demo.service.common.code;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.common.code.CommonCode;

public interface CommonCodeService {

    /** 공통코드 전체 리로딩 **/
    void selectAll();

    /** 여러 개의 그룹 코드에 해당하는 공통 코드 목록 조회 **/
    Map<String, List<CommonCode>> selectByGroupCodes(List<String> groups);

    /** 단일 그룹 코드 조회 (테스트용) **/
    List<CommonCode> selectByGroupCode(String groupCode);
}