package com.example.demo.web.common.code;


import com.example.demo.domain.common.code.CommonCode;
import com.example.demo.service.common.code.impl.CommonCodeServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common-code")
public class CommonCodeController {

    private final CommonCodeServiceImpl commonCodeService;

    /** 여러 개 그룹의 공통코드 목록 조회 **/
    @PostMapping
    public Map<String, List<CommonCode>> getCommonCodes(
            @RequestBody List<String> groups
    ) {
        return commonCodeService.selectByGroupCodes(groups);
    }
    
    /** 단일 그룹의 공통코드 목록 조회 (개발 시 테스트용). 경로 변수 타입 **/
    @GetMapping("/{groupCode}")
    public List<CommonCode> getCommonCodesByPathVariable(
            @PathVariable("groupCode") String groupCode
    ) {
        return commonCodeService.selectByGroupCode(groupCode);
    }

    /** 단일 그룹의 공통코드 목록 조회 (개발 시 테스트용). 쿼리 파라미터 타입**/
    @GetMapping("/single")
    public List<CommonCode> getCommonCodesByRequestParam(
            @RequestParam("groupCode") String groupCode
    ) {
        
    	return commonCodeService.selectByGroupCode(groupCode);
    }
}
