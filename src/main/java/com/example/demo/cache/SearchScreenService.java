package com.example.demo.cache;

import com.example.demo.business.search.cache.SearchConditionCodeCache;
import com.example.demo.business.search.mapper.CommonCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchScreenService {

    private static final String SCREEN_ID = "SEARCH001";

    private final SearchConditionCodeCache cache;
    private final CommonCodeMapper commonCodeMapper;

    public Map<String, List<Map<String, Object>>> loadSearchConditionCodes() {

        List<String> groupCodes = List.of(
            "code_a",
            "code_b",
            "code_c"
        );

        Map<String, List<Map<String, Object>>> result = new LinkedHashMap<>();

        for (String groupCode : groupCodes) {
            result.put(
                groupCode,
                cache.getOrLoad(
                    SCREEN_ID,          // Service 내부 상수
                    groupCode,
                    () -> commonCodeMapper.selectByGroupCode(groupCode)
                )
            );
        }

        return result;
    }
}