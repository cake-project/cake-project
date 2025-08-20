package com.cakemate.cake_platform.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeConverter {

    // 인스턴스화 방지(객체 못 만들게)
    private TimeConverter() {}

    // UTC -> KST 변환
    public static LocalDateTime utcToKst(LocalDateTime utcTime) {
        if (utcTime == null) return null;
        ZonedDateTime utcZoned = utcTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime kstZoned = utcZoned.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
        return kstZoned.toLocalDateTime();
    }
}
