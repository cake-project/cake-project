package com.cakemate.cake_platform.domain.store.ranking.service;

import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreOrderCount;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreRankingResponseDto;
import lombok.extern.slf4j.Slf4j;
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

    public StoreRankingService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    // 기존 요청용 메서드 (Cache-Aside)
    @Cacheable(value = "storeOrderRankings")
    @Transactional(readOnly = true)
    public List<StoreRankingResponseDto> getWeeklyTopStores() {
        return calculateWeeklyTopStores();
    }

    // 스케줄러용 캐시 강제 갱신 메서드
    @CachePut(value = "storeOrderRankings")
    @Transactional(readOnly = true)
    public List<StoreRankingResponseDto> refreshWeeklyTopStores() {
        return calculateWeeklyTopStores();
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
