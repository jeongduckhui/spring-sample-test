package com.example.demo.sqlprovider;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.example.demo.domain.dynamic.MenuColumn;
import com.example.demo.domain.dynamic.MenuColumnFilter;

public class MenuColumnsSqlProvider {
	
	// 테이블 이름을 상수로 정의하여 재사용성을 높입니다. (2000줄 쿼리 시 유용)
    private static final String TABLE_NAME = "X_MENU";

	public String getMenuDataQuery(Map<String, Object> params) {
		
		// 1. 파라미터 추출
		// @Param("filter")로 전달된 ColumnFilter 객체를 가져옴
		MenuColumnFilter filter = (MenuColumnFilter) params.get("filter");
		Long targetId = (Long) params.get("id"); 
				
		// 2. SQL 빌더를 사용하여 쿼리 전체 조립
		String finalSql = new SQL() {{
			
			// 2-1. SELECT 절 (동적 컬럼 생성 로직)
            // 선택된 컬럼이 없는지 확인하기 위한 임시 변수
            boolean hasSelectedColumn = false;
            
            if(filter != null) {
            	if(filter.isMENU_ID()) {
    				SELECT(MenuColumn.MENU_ID.getColumnName());
    				hasSelectedColumn = true;
    			}
    			if(filter.isCOMPONENT()) {
    				SELECT(MenuColumn.COMPONENT.getColumnName());
    				hasSelectedColumn = true;
    			}
    			if(filter.isPATH()) {
    				SELECT(MenuColumn.PATH.getColumnName());
    				hasSelectedColumn = true;
    			}
    			if(filter.isREDIRECT()) {
    				SELECT(MenuColumn.REDIRECT.getColumnName());
    				hasSelectedColumn = true;
    			}
    			if(filter.isNAME()) {
    				SELECT(MenuColumn.NAME.getColumnName());
    				hasSelectedColumn = true;
    			}
    			if(filter.isTITLE()) {
    				SELECT(MenuColumn.TITLE.getColumnName());
    				hasSelectedColumn = true;
    			}
    			if(filter.isICON()) {
    				SELECT(MenuColumn.ICON.getColumnName());
    				hasSelectedColumn = true;
    			}
    			if(filter.isPARENT_ID()) {
    				SELECT(MenuColumn.PARENT_ID.getColumnName());
    				hasSelectedColumn = true;
    			}
    			if(filter.isIS_LEAF()) {
    				SELECT(MenuColumn.IS_LEAF.getColumnName());
    				hasSelectedColumn = true;
    			}
    			if(filter.isHIDDEN()) {
    				SELECT(MenuColumn.HIDDEN.getColumnName());
    				hasSelectedColumn = true;
    			}
    			
    			// 필터가 null이거나 선택된 컬럼이 하나도 없다면 SELECT(*)
                if (filter == null || !hasSelectedColumn) {
                    SELECT("*");
                }
                
             // 2-2. FROM 절
                FROM(TABLE_NAME);

                // 2-3. WHERE 절 (기존 쿼리 형태 유지 및 안전한 바인딩)
                WHERE("1=1");
                if (targetId != null) {
                    // #{id}는 안전하게 PreparedStatement로 바인딩됩니다.
                    AND().WHERE(TABLE_NAME + ".menu_id = #{id}"); 
                }
            }
			
			
		}}.toString();
		
		return finalSql;
	}
}
























