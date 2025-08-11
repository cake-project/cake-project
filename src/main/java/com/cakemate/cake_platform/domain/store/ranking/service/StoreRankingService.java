package com.cakemate.cake_platform.domain.store.ranking.service;

import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreRankingResponseDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StoreRankingService {
    private final OrderRepository orderRepository;

    public StoreRankingService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Cacheable(value = "storeOrderRankings")
    public List<StoreRankingResponseDto> getWeeklyTopStores() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        return orderRepository.findWeeklyTopStores(startDate);
    }
}
