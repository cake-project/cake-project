package com.cakemate.cake_platform.domain.order.repository;

import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByRequestFormCustomerId(Long customerId, Pageable pageable);

    boolean existsByRequestForm(RequestForm requestForm);
}
