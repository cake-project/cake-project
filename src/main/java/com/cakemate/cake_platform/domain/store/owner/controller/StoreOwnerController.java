package com.cakemate.cake_platform.domain.store.owner.controller;

import com.cakemate.cake_platform.domain.store.owner.dto.StoreCreateRequestDto;
import com.cakemate.cake_platform.domain.store.owner.service.StoreOwnerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StoreOwnerController {
    private final StoreOwnerService storeOwnerService;

    public StoreOwnerController(StoreOwnerService storeOwnerService) {
        this.storeOwnerService = storeOwnerService;
    }
    @PostMapping("/owner/stores")
    public void createStore(@RequestBody StoreCreateRequestDto requestDto) {

    }
}
