package com.example.demo.web;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.dynamic.DynamicColumnService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dynamic")
@RequiredArgsConstructor
public class DynamicColumnController {

	private final DynamicColumnService dynamicColumnService;
	
	@PostMapping("/data")
	public List<Map<String, Object>> getMenuData(@RequestBody Map<String, Object> params) {
		List<Map<String, Object>> result = dynamicColumnService.getMenuDataFromMap(params);
		
		return result;
	}
}
