package com.cakemate.cake_platform.domain.order.owner.controller;

import com.cakemate.cake_platform.domain.order.owner.service.OrderOwnerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class OrderOwnerController {

    private final OrderOwnerService ownerService;

    public OrderOwnerController(OrderOwnerService ownerService) {
        this.ownerService = ownerService;
    }
}
