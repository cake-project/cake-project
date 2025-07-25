package com.cakemate.cake_platform.domain.order.owner.service;

import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderOwnerService {

    private final OrderRepository orderRepository;

    public OrderOwnerService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
}
