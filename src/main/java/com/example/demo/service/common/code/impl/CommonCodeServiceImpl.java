package com.example.demo.service.common.code.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.domain.common.code.CommonCode;
import com.example.demo.repository.common.code.CommonCodeMapper;
import com.example.demo.service.common.code.CommonCodeService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService{

	private final CommonCodeMapper commonCodeMapper;
	
	// 스레드 안전한 공통코드 캐시 저장소 Map
	// 파일럿 프로젝트용으로 Map으로 캐시 구현. 실무 때는 필요없음.
	private final Map<String, List<CommonCode>> cache = new ConcurrentHashMap<>();
	// TTL(Time-To-Live) 초기화. 마지막 캐시 로드 시간 추적용.
	private Instant lastLoadedTime = Instant.MIN;
	
	// Service 생성자 호출 후 자동으로 전체 공통코드 로드할 지 여부(default 값 true) 
	@Value("${common-code.init-enabled:true}")
	private boolean initEnabled;
	// 캐시 데이터 만료시간 (default 값 5분)
	@Value("${common-code.ttl-seconds:300}")
    private long ttlSeconds;
	
	/** 생성자 호출 후 공통코드 자동 초기화 **/
	@PostConstruct
	public void initCommonCode() {
		if(!initEnabled) {
			log.info("[COMMON_CODE] 설정값에 의해 공통코드 자동 초기화가 비활성화되었습니다.");
            return;
		}
		
		// 공통코드 전체 리로딩
		selectAll();
	}
	
	/** 공통코드 전체 리로딩 **/
	@Override
	public synchronized void selectAll() {
		log.info("[COMMON_CODE] 공통코드 자동 초기화를 시작합니다.");
		
		// 화면에 보이는 조회조건 전체 공통코드만 조회 (숨겨져있는 조회조건 공통코드는 조회하지 않음)
		List<CommonCode> list = commonCodeMapper.selectAll();
        cache.clear();
        
        // 조회한 공통코드를 루프문으로 돌리면서 캐시에 있는 CommonCode와 키값을 비교
        list.forEach(c -> {
        	// 캐시에 키값이 없다면 새로운 ArrayList를 생성해 해당 키값의 CommonCode를 추가하고 ArrayList를 반환하여 List<CommonCode>에 추가
            cache.computeIfAbsent(c.getGroupCode(), k -> new ArrayList<>()).add(c);
        });
        
        // 리로드된 시간을 갱신
        lastLoadedTime = Instant.now();
        
        log.info("[COMMON_CODE] 공통코드 자동 초기화가 완료되었습니다: {} 개 공통코드 리로드", cache.size());
	}
	
	/** 여러 개의 그룹 코드에 해당하는 공통 코드 목록 조회 **/
	@Override
    public synchronized Map<String, List<CommonCode>> selectByGroupCodes(List<String> groups) {
    	// 캐싱 데이터의 유효시간이 만료된 경우, 전체 공통코드 리로드
    	// ****** 실제 적용할 때는 selectAll에 화면에 보이는 조회조건만 where 절 조건으로 넣고, 나머지 숨겨진 조회조건은 직접 call 해서 조회하게 개발해야 함. ******
        if (isExpired()) {
            log.info("[COMMON_CODE] 공통코드 캐싱 데이터의 유효시간이 만료되어 전체 공통코드를 리로드합니다.");
            
            // 공통코드 전체 조회
            selectAll();
        }

        // 파라미터로 받은 공통코드의 키값 중 캐시에 없는 키값 그룹 탐색
        List<String> missingGroups = new ArrayList<>();
        for (String g : groups) {
            if (!cache.containsKey(g)) {
            	// 캐시에 없는 공통코드 따로 저장
                missingGroups.add(g);
            }
        }

        // 파라미터로 받은 공통코드의 키값 중 캐시에 없는 공통코드만 DB에서 조회
        if (!missingGroups.isEmpty()) {
            log.info("[COMMON_CODE] 공통코드 캐싱 데이터에 없는 공통코드를 DB 조회합니다.: {}", missingGroups);
            
            // 여러 개의 그룹 코드에 해당하는 공통 코드 목록 조회
            List<CommonCode> dbList = commonCodeMapper.selectByGroupCodes(missingGroups);

            // 조회한 공통코드를 루프문으로 돌리면서 캐시에 있는 CommonCode와 키값을 비교
            dbList.forEach(c ->
            	// 캐시에 키값이 없다면 새로운 ArrayList를 생성해 해당 키값의 CommonCode를 추가하고 ArrayList를 반환하여 List<CommonCode>에 추가
            	cache.computeIfAbsent(c.getGroupCode(), k -> new ArrayList<>()).add(c)
            );
        }

        // 반환 데이터 구성
        Map<String, List<CommonCode>> result = new LinkedHashMap<>();
        // 파라미터로 받은 공통코드 키값을 루프문으로 돌려 캐시 데이터와 비교해서 캐시에 잇으면 캐싱된 값 리턴, 없으면 빈 리스트 리턴
        groups.forEach(g -> result.put(g, cache.getOrDefault(g, List.of())));

        return result;
    }
    
    /** 단일 그룹의 공통코드 조회 (개발 시 테스트용) **/
	@Override
    public List<CommonCode> selectByGroupCode(String groupCode) {
    	return commonCodeMapper.selectByGroupCode(groupCode);
    }
	
	/** 캐시 데이터 만료 여부 체크 **/
	private boolean isExpired() {
		// 캐시 데이터가 마지막으로 로드된 시점에서 캐시 유효시간(설정값: 5분)이 지났는지 체크
		return Instant.now().isAfter(lastLoadedTime.plusSeconds(ttlSeconds));
	}
}




















