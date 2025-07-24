package com.cakemate.cake_platform.domain.order.entity;

import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자에게 보여줄 주문 번호 입니다.
    @Column(unique = true, nullable = false)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestForm_id", nullable = false)
    private RequestForm requestForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalForm_id", nullable = false)
    private ProposalForm proposalForm;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    // 주문 정보를 검색할 때 고객 이름을 빨리 검색하기 위해 사용하는 필드입니다.
    // 주문 당시 고객 이름과 의뢰서에 있는 고객 이름이 달라질 경우 사용할 수 있습니다.
    @Column(nullable = false)
    private String customerName;

//    추후 payment table 생성 시
//    private String payerName;

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
}
