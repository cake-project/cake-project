package com.cakemate.cake_platform.domain.order.entity;

import com.cakemate.cake_platform.common.entity.BaseTimeEntity;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.cakemate.cake_platform.domain.order.owner.exception.InvalidOrderStatusException;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.store.entity.Store;
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
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자에게 보여줄 주문 번호 입니다.
    @Column(unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_form_id", nullable = false)
    private RequestForm requestForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_form_id", nullable = false)
    private ProposalForm proposalForm;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    // 주문 정보를 검색할 때 고객 이름을 빨리 검색하기 위해 사용하는 필드입니다.
    // 주문 당시 고객 이름과 의뢰서에 있는 고객 이름이 달라질 경우 사용할 수 있습니다.
    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerPhoneNumber;

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

    // 주문 상태 변경 메서드
    public void updateOrderStatus(OrderStatus newStatus) {
        if (this.status == OrderStatus.COMPLETED) {
            throw new InvalidOrderStatusException("이미 완료된 주문입니다.");
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException("취소된 주문은 상태를 변경할 수 없습니다.");
        }
        if (newStatus == this.status) {
            throw new InvalidOrderStatusException("현재 상태와 동일한 상태로는 변경할 수 없습니다.");
        }

        switch (this.status) {
            case MAKE_WAITING -> {
                if (newStatus != OrderStatus.READY_FOR_PICKUP && newStatus != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStatusException("현재 상태: " + this.status + ", 변경 요청 상태: " + newStatus + ". 이 상태로는 변경할 수 없습니다.");
                }
            }

            case READY_FOR_PICKUP -> {
                if (newStatus == OrderStatus.CANCELLED) {
                    throw new InvalidOrderStatusException("주문 취소는 '대기중(MAKE_WAITING)' 상태에서만 가능합니다.");
                }
                if (newStatus != OrderStatus.COMPLETED) {
                    throw new InvalidOrderStatusException("현재 상태: " + this.status + ", 변경 요청 상태: " + newStatus + ". 이 상태로는 변경할 수 없습니다.");
                }
            }
            default ->
                    throw new InvalidOrderStatusException("현재 상태: " + this.status + ", 변경 요청 상태: " + newStatus + ". 주문 상태는 한 단계씩 앞으로만 변경할 수 있으며, 이전 단계로는 되돌릴 수 없습니다.");
        }

        this.status = newStatus;
    }

}
