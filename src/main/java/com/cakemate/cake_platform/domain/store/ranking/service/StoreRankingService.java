package com.cakemate.cake_platform.domain.store.ranking.service;

import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreOrderCount;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreRankingResponseDto;
import lombok.extern.slf4j.Slf4j;
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
//    @Cacheable(value = "storeOrderRankings")
    @Transactional(readOnly = true)
    public List<StoreRankingResponseDto> getWeeklyTopStores() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        // 1. 주문 수 기준 내림차순 정렬된 데이터 조회
        long start = System.currentTimeMillis();
        List<StoreOrderCount> storeOrderCounts = orderRepository.findWeeklyTopStores(startDate);
        long end = System.currentTimeMillis();
        log.info("getStoreRanking 처리 시간: {} ms", (end - start));
        //순위 매기기

        List<StoreRankingResponseDto> storeRankingResponseDtoList = new ArrayList<>();

        long rank = 0;              //현재 순위
        long prevOrderCount = -1;   //이전 주문 수
        int sameRankCount = 0;        // 동점 그룹 크기

        for (int i = 0; i < storeOrderCounts.size(); i++) {
            StoreOrderCount storeOrderCount = storeOrderCounts.get(i);

            if (storeOrderCount.getOrderCount() != prevOrderCount) {
                //주문 수가 이전과 다르면, 현재 인덱스+1에서 이전 동점자 수만큼 뺸 값으로 순위 부여
                rank += sameRankCount + 1;
                sameRankCount = 0;
                prevOrderCount = storeOrderCount.getOrderCount();
            } else {
                // 이전과 같은 주문 수 => 동점자 수 증가, rank 유지
                sameRankCount++;
            }
            storeRankingResponseDtoList.add(new StoreRankingResponseDto(
                    rank,
                    storeOrderCount.getStoreId(),
                    storeOrderCount.getStoreName(),
                    storeOrderCount.getOrderCount()
            ));
            if (storeRankingResponseDtoList.size() == 10) {
                break;  // 10개 채우면 루프 종료
            }
        }
        return  storeRankingResponseDtoList;
    }
}
