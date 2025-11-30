package com.example.demo.repository.dynamic;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.example.demo.domain.dynamic.MenuColumnFilter;
import com.example.demo.sqlprovider.MenuColumnsSqlProvider;

@Mapper
public interface MenuDynamicColumnMapper {

	/**
     * XML 쿼리를 무시하고 MenuColumnsSqlProvider의 getMenuDataQuery 메서드를 호출합니다.
     * 이 메서드는 SELECT 쿼리 전체를 문자열로 반환합니다.
     * @param filter 조회할 컬럼을 정의하는 필터 객체
     * @param id WHERE 절에 사용될 메뉴 ID
     * @return 조회 결과 (Map 형태)
     */
	@SelectProvider(type = MenuColumnsSqlProvider.class, method = "getMenuDataQuery")
    List<Map<String, Object>> selectMenuData(
        @Param("filter") MenuColumnFilter filter, 
        @Param("id") Long id
    );
}
