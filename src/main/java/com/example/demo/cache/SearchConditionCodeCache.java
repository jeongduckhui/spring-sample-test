package com.example.demo.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Component
public class SearchConditionCodeCache {

    private final boolean cacheEnabled;
    private final long ttlMillis;

    private final ConcurrentHashMap<String, CacheEntry> cache =
            new ConcurrentHashMap<>();

    public SearchConditionCodeCache(
            @Value("${search.condition.cache.enabled:true}") boolean cacheEnabled,
            @Value("${search.condition.cache.ttl-minutes:1440}") long ttlMinutes
    ) {
        this.cacheEnabled = cacheEnabled;
        this.ttlMillis = ttlMinutes * 60 * 1000;
    }

    /**
     * 화면 ID + 공통코드 키 기반 캐시 조회
     *
     * @param screenId  화면 ID (예: SEARCH001)
     * @param groupCode 공통코드 키 (예: code_a)
     * @param loader    DB 조회 로직
     */
    public List<Map<String, Object>> getOrLoad(
            String screenId,
            String groupCode,
            Supplier<List<Map<String, Object>>> loader
    ) {
        // 캐시 OFF → 바로 DB 조회
        if (!cacheEnabled) {
            return loader.get();
        }

        String cacheKey = buildCacheKey(screenId, groupCode);
        long now = System.currentTimeMillis();

        CacheEntry entry = cache.get(cacheKey);

        if (entry == null || entry.isExpired(now)) {
            return cache.compute(cacheKey, (key, oldEntry) -> {
                if (oldEntry == null || oldEntry.isExpired(now)) {
                    return new CacheEntry(loader.get(), now + ttlMillis);
                }
                return oldEntry;
            }).value();
        }

        return entry.value();
    }

    /**
     * 캐시키 생성 책임은 Cache에만 둔다
     */
    private String buildCacheKey(String screenId, String groupCode) {
        return groupCode + "_" + screenId;
    }

    public void clear() {
        cache.clear();
    }

    /* ================= 내부 캐시 엔트리 ================= */

    private record CacheEntry(
            List<Map<String, Object>> value,
            long expireAt
    ) {
        boolean isExpired(long now) {
            return expireAt < now;
        }
    }
}