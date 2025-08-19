package com.cakemate.cake_platform.domain.store.ranking.service;

import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreOrderCount;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreRankingResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class StoreRankingService {
    private final OrderRepository orderRepository;
    private final CacheManager cacheManager;
    private static final String CACHE_KEY = "storeOrderRankings";
    private static final String CACHE_NAME = "storeOrderRankings";

    public StoreRankingService(OrderRepository orderRepository, CacheManager cacheManager) {
        this.orderRepository = orderRepository;
        this.cacheManager = cacheManager;
    }


    // 기존 요청용 메서드 (캐시 확인 + DB fallback)
    @Transactional(readOnly = true)
    public List<StoreRankingResponseDto> getWeeklyTopStores() {
        List<StoreRankingResponseDto> cached = getCacheIfExists();
        if (cached != null) {
            return cached;
        }
        return refreshWeeklyTopStores(); // 캐시 없으면 DB 조회 후 저장
    }

    // 캐시 강제 갱신
    @Transactional(readOnly = true)
    public List<StoreRankingResponseDto> refreshWeeklyTopStores() {
        List<StoreRankingResponseDto> data = calculateWeeklyTopStores();

        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            cache.put(CACHE_KEY, data); // @CachePut 대신 직접 저장
            log.info("[서비스] Redis 캐시에 값 저장 완료");
        } else {
            log.warn("[서비스] Redis 캐시 객체 없음");
        }

        return data;
    }

    // Redis 캐시 존재 여부 확인
    public List<StoreRankingResponseDto> getCacheIfExists() {
        Cache cache = cacheManager.getCache(CACHE_NAME);
        if (cache != null) {
            Object cachedObj = cache.get(CACHE_KEY, Object.class); // Object로 꺼내기
            if (cachedObj instanceof List<?> list) {               // 런타임에서 타입 체크
                List<StoreRankingResponseDto> cached = list.stream()
                        .filter(StoreRankingResponseDto.class::isInstance)
                        .map(StoreRankingResponseDto.class::cast)
                        .toList();
                return cached;
            }
        }
        log.info("[서비스] Redis 캐시 miss ❌");
        return null;
    }

    // 실제 순위 계산 로직 분리
    private List<StoreRankingResponseDto> calculateWeeklyTopStores() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        long start = System.currentTimeMillis();
        List<StoreOrderCount> storeOrderCounts = orderRepository.findWeeklyTopStores(startDate);
        long end = System.currentTimeMillis();
        log.info("getStoreRanking 처리 시간: {} ms", (end - start));

        List<StoreRankingResponseDto> storeRankingResponseDtoList = new ArrayList<>();
        long rank = 0;
        long prevOrderCount = -1;
        int sameRankCount = 0;

        for (int i = 0; i < storeOrderCounts.size(); i++) {
            StoreOrderCount storeOrderCount = storeOrderCounts.get(i);

            if (storeOrderCount.getOrderCount() != prevOrderCount) {
                rank += sameRankCount + 1;
                sameRankCount = 0;
                prevOrderCount = storeOrderCount.getOrderCount();
            } else {
                sameRankCount++;
            }

            storeRankingResponseDtoList.add(new StoreRankingResponseDto(
                    rank,
                    storeOrderCount.getStoreId(),
                    storeOrderCount.getStoreName(),
                    storeOrderCount.getOrderCount()
            ));

            if (storeRankingResponseDtoList.size() == 10) break;
        }

        return storeRankingResponseDtoList;
    }
}
