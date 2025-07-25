package com.cakemate.cake_platform.domain.order.repository;

import com.cakemate.cake_platform.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByRequestFormCustomerId(Long customerId, Pageable pageable);

    Optional<Order> findByCustomerIdAndId(Long customerId, Long id);

}
