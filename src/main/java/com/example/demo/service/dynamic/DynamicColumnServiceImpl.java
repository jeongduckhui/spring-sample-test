package com.example.demo.service.dynamic;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.domain.dynamic.MenuColumnFilter;
import com.example.demo.repository.dynamic.MenuDynamicColumnMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DynamicColumnServiceImpl implements DynamicColumnService{
	
	private final MenuDynamicColumnMapper menuDynamicColumnMapper;
	
	/**
     * Map 파라미터를 받아 ColumnFilter를 동적으로 생성하고 데이터를 조회합니다.
     * @param params 컨트롤러에서 넘겨받은 Map ({ "columns": [...], "id": 100 })
     */
	@Override
	public List<Map<String, Object>> getMenuDataFromMap(Map<String, Object> params) {
		
		// 1. Map에서 필요한 데이터 추출 (타입 검증 포함)
		Object columnsObj = params.get("columns");
		Long targetId = ((Number) params.get("id")).longValue();
		
		if(columnsObj == null || !(columnsObj instanceof Collection)) {
			// 컬럼이 제공되지 않으면 모든 컬럼을 조회하거나 빈 리스트를 반환할 수 있음.
			// 일단 컬럼이 넘어오지 않으면 컬럼 리스트는 필수라고 전제하고 예외로 처리. 
			throw new IllegalArgumentException("요청 파라미터에 유효한 'columns' 배열이 필요합니다.");
		}
		
		@SuppressWarnings("unchecked")
        List<String> requestedColumns = ((Collection<String>) columnsObj).stream()
                                            .filter(s -> s != null)
                                            .toList();
		
		// 2. ColumnFilter 동적 생성 (요청된 컬럼만 true로 설정)
		MenuColumnFilter filter = createColumnFilter(requestedColumns);
		
		// 여기서부터 수정해야 함.
		
		// 3. Mapper 호출
        // MenuColumnsSqlProvider.getMenuDataQuery가 이 filter와 menuId를 받아서 쿼리를 생성합니다.
        List<Map<String, Object>> result = menuDynamicColumnMapper.selectMenuData(filter, targetId);
        
        
//		 List<Map<String, Object>> result = menuDynamicColumnMapper.selectMenuData(filter, targetId);
		 System.out.println("##############  result: "+ result);
		 return result;
	}
	
	/** Reflection을 사용하여 ColumnFilter 객체의 필드를 동적으로 설정 */
    private MenuColumnFilter createColumnFilter(List<String> requestedColumns) {
    	MenuColumnFilter filter = new MenuColumnFilter();
    	
    	for(String colName : requestedColumns) {
    		try {
    			// 문자열 컬럼명과 일치하는 필드를 찾아 true로 설정
				Field field = MenuColumnFilter.class.getDeclaredField(colName);
				field.setAccessible(true);
				field.set(filter, true);
			} catch (NoSuchFieldException e) {
				// DTO에 정의되지 않은 컬럼명은 무시
				// 일단 컬럼명이 유효하지 않으면 로그에만 남기고 정상 처리
				// 실제로 코딩할 때는 로그로 남겨야 함. 
                System.err.println("경고: 무시된 유효하지 않은 컬럼명: " + colName);
			} catch (Exception e) {
				// NoSuchFieldException 외의 예외가 발생하면 
				// RuntimeException으로 감싸 예외 던짐
				throw new RuntimeException("컬럼 필터 설정 중 오류 발생: " + colName, e);
			}
    	}
    	
    	return filter;
    }

}




















































