package com.cakemate.cake_platform.domain.order.entity;

import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "requestForm_id")
    private RequestForm requestForm;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalForm_id")
    private ProposalForm proposalForm;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;


    protected Order() {}

    public Order(Long id, RequestForm requestForm, ProposalForm proposalForm, OrderStatus status) {
        this.id = id;
        this.requestForm = requestForm;
        this.proposalForm = proposalForm;
        this.status = status;
    }
}
