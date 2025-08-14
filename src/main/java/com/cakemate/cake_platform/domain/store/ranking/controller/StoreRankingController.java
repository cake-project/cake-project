package com.cakemate.cake_platform.domain.store.ranking.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreRankingResponseDto;
import com.cakemate.cake_platform.domain.store.ranking.service.StoreRankingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api")
public class StoreRankingController {
    private final StoreRankingService storeRankingService;

    public StoreRankingController(StoreRankingService storeRankingService) {
        this.storeRankingService = storeRankingService;
    }
    @GetMapping("/stores/rankings")
    public ResponseEntity<ApiResponse<List<StoreRankingResponseDto>>> getWeeklyOrderRankings() {

        List<StoreRankingResponseDto> storeRankingResponseDtoList = storeRankingService.getWeeklyTopStores();

        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, "최근 1주일 주문량 랭킹을 성공적으로 불러왔습니다.", storeRankingResponseDtoList)
        );
    }
}
