package com.example.demo.service.dynamic;

import java.util.List;
import java.util.Map;

public interface DynamicColumnService {

	public List<Map<String, Object>> getMenuDataFromMap(Map<String, Object> params); 
}
