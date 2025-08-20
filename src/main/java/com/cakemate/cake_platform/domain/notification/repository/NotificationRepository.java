package com.cakemate.cake_platform.domain.notification.repository;

import com.cakemate.cake_platform.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
//    //모든 알림 조회(최신순)
//    List<Notification> findAllByReceiverIdOrderByCreatedAtDesc(Long receiverid);
//
//    //읽지 않은 알림 조회(최신순)
//    List<Notification> findAllByReceiverIdAndReadIsFalseOrderByCreatedAtDesc(Long receiverId);

    //특정 수신자의 receiverType + createdAt 이후 알림 조회
    List<Notification> findByReceiverIdAndMemberTypeAndCreatedAtAfter(
            Long receiverId, String memberType, LocalDateTime createdAt
    );

    //특정 수신자의 receiverType + lastEventId 이후 알림 조회
    List<Notification> findByReceiverIdAndMemberTypeAndIdGreaterThan(
            Long receiverId, String memberType, Long lastEventId
    );

    //재연결 시 특정 사용자 lastEventId 이후 알림 조회
    List<Notification> findByReceiverIdAndMemberTypeAndIdGreaterThanOrderByIdAsc(
            Long receiverId, String memberType, Long lastEventId
    );
}
