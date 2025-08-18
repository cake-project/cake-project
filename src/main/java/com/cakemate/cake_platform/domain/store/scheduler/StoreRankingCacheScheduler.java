package com.cakemate.cake_platform.domain.store.scheduler;

import com.cakemate.cake_platform.domain.store.ranking.dto.StoreRankingResponseDto;
import com.cakemate.cake_platform.domain.store.ranking.service.StoreRankingService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class StoreRankingCacheScheduler {
    private final StoreRankingService storeRankingService;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // TTL 및 마진 설정 (운영 환경: TTL 60분, 마진 5분)
    private static final long TTL_MINUTES = 5;
    private static final long MARGIN_MINUTES = 1;

    public StoreRankingCacheScheduler(StoreRankingService storeRankingService) {
        this.storeRankingService = storeRankingService;
    }

    @PostConstruct
    public void init() {
        try {
            List<StoreRankingResponseDto> cached = storeRankingService.getCacheIfExists();
            if (cached == null) {
                storeRankingService.refreshWeeklyTopStores(); // 캐시 없으면 DB 조회 후 저장
                log.info("[스케줄러] 서버 시작 시 캐시 초기 로드 완료");
            } else {
                log.info("[스케줄러] Redis 캐시 존재, DB 조회 생략");
            }
        } catch (Exception e) {
            log.error("[스케줄러] 서버 시작 시 캐시 로드 실패", e);
        }

        scheduleNextRun();
    }

    /**
     * 다음 실행 예약 (TTL 기준)
     */
    private void scheduleNextRun() {
        long delayMinutes = TTL_MINUTES - MARGIN_MINUTES; // TTL 만료 직전 마진
        scheduler.schedule(this::prewarmCache, delayMinutes, TimeUnit.MINUTES);
    }

    /**
     * 캐시 강제 갱신
     */
    private void prewarmCache() {
        try {
            storeRankingService.refreshWeeklyTopStores(); // @CachePut → 항상 캐시 갱신
            log.info("[스케줄러] 가게 랭킹 캐시가 TTL 만료 전에 갱신되었습니다.");
        } catch (Exception e) {
            log.error("[스케줄러] 캐시 갱신에 실패했습니다.", e);
        }
        // 다음 실행 예약
        scheduleNextRun();
    }
}

