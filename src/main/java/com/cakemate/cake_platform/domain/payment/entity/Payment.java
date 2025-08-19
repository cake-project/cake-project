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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    private String paymentKey;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String method;

    private String easyPayProvider;

    private String receiptUrl;

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;


    /**
     * JPA 에서 사용하는 기본 생성자 입니다.
     */
    protected Payment() {
    }

    public Payment(Order order, int amount, String method, PaymentStatus paymentStatus) {
        this.order = order;
        this.amount = amount;
        this.method = method;
        this.paymentStatus = paymentStatus;
    }

    public Payment updatePayment(PaymentStatus paymentStatus, String paymentKey, int amount, String method, String easyPayProvider, String receiptUrl, LocalDateTime requestedAt, LocalDateTime approvedAt) {
        this.paymentStatus = paymentStatus;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.method = method;
        this.easyPayProvider = easyPayProvider;
        this.receiptUrl = receiptUrl;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        return this;
    }
}
