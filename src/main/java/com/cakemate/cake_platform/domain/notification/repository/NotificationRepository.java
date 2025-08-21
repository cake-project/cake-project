package com.cakemate.cake_platform.domain.notification.repository;

import com.cakemate.cake_platform.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    //모든 알림 조회(최신순)
    List<Notification> findAllByReceiverIdOrderByCreatedAtDesc(Long receiverid);

    //읽지 않은 알림 조회(최신순)
    List<Notification> findAllByReceiverIdAndReadIsFalseOrderByCreatedAtDesc(Long receiverId);

    //lastEventId 이후~하루 이내 알림 조회
    List<Notification> findByReceiverIdAndIdGreaterThanAndCreatedAtAfterOrderByIdAsc(
            Long receiverId, Long lastEvevntId, LocalDateTime after
    );

    //특정 lastEventId 이후 그 ID보다 큰 ID를 가진 알림이 DB에 존재하는지 확인
    boolean existsByIdGreaterThan(Long id);

}
