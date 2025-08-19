package com.cakemate.cake_platform.domain.order.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OrderNumberGenerator {

    // 주문번호에 붙일 시퀀스 번호 입니다.(0부터 시작) -> 최대값 999(3자리)
    private static int sequence = 0;
    private static final int MAX_SEQ = 999;

    // 주문번호 만드는 메서드 입니다. synchronized -> (동기화, 여러 스레드가 동시에 호출해도 한 번에 한 스레드만 실행)
    public static synchronized String generateOrderNumber() {
        // 현재 날짜, 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        String timePart = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 시퀀스 번호가 999 이상이면 0으로 초기화
        if (sequence >= MAX_SEQ) {
            sequence = 0;
        }
        sequence++;

        // 시간 문자열 + 3자리 시퀀스 번호 합쳐서 주문번호로 반환
        return timePart + String.format("%03d", sequence);
    }
}
