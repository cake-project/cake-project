package com.cakemate.cake_platform.domain.order.entity;

import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestForm_id", nullable = false)
    private RequestForm requestForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalForm_id", nullable = false)
    private ProposalForm proposalForm;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String payerName;

    @Column(nullable = false)
    private String storeBusinessName;

    @Column(nullable = false)
    private String storeName;

    private String productName;

    @Column(nullable = false)
    private String storePhoneNumber;

    @Column(nullable = false)
    private String storeAddress;

    @Column(nullable = false)
    private int agreedPrice;

    @Column(nullable = false)
    private LocalDateTime agreedPickupDate;

    private String finalCakeImage;

    protected Order() {
    }

    public Order(RequestForm requestForm, ProposalForm proposalForm, OrderStatus status) {
        this.requestForm = requestForm;
        this.proposalForm = proposalForm;
        this.status = status;
    }
}
