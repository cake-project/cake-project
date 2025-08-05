package com.cakemate.cake_platform.domain.payment.entity;

import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String version;

    private String type;

    private String paymentKey;

    private int amount;

    private String method;

    private PaymentStatus paymentStatus;

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    private String failReason;

    private String cancelReason;

    /**
     * JPA 에서 사용하는 기본 생성자 입니다.
     */
    protected Payment() {
    }
}
