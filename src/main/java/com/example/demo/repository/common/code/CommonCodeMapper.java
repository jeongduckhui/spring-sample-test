package com.example.demo.repository.common.code;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.common.code.CommonCode;

import java.util.List;

@Mapper
public interface CommonCodeMapper {

	// 화면에 보이는 조회조건 전체 공통코드만 조회 (숨겨져있는 조회조건 공통코드는 조회하지 않음)
    List<CommonCode> selectAll();
    
    // 여러 개의 그룹 코드에 해당하는 공통 코드 목록 조회
    List<CommonCode> selectByGroupCodes(@Param("groupCodes") List<String> groupCodes);
    
    // 단 하나의 특정 그룹 코드에 해당하는 공통 코드 목록 조회
    List<CommonCode> selectByGroupCode(@Param("groupCode") String groupCode);
}