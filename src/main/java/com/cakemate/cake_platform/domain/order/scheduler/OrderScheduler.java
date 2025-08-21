package com.cakemate.cake_platform.domain.order.scheduler;

import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class OrderScheduler {

    private final OrderRepository orderRepository;

    public OrderScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Scheduled(fixedRate = 300000) // 1분마다 실행
    public void checkExpiredOrders() {

        // 만료 시간이 지난 주문 리스트 가져오기
        List<Order> expiredOrders = orderRepository.findByStatusAndPaymentExpiresAtBefore(OrderStatus.MAKE_WAITING, LocalDateTime.now());

        log.info("[OrderScheduler] 만료된 주문 수: {}개", expiredOrders.size());

        for (Order order : expiredOrders) {
            order.setStatus(OrderStatus.EXPIRED);
        }

        orderRepository.saveAll(expiredOrders);

        log.info("[OrderScheduler] 상태 업데이트 완료");
    }
}
