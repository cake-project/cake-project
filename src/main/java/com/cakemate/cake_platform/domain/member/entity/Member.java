package com.cakemate.cake_platform.domain.member.entity;

import com.cakemate.cake_platform.domain.owner.entity.Owner;
import com.cakemate.cake_platform.domain.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "members")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    protected Member() {}

    public Member(Long id, Customer customerId, Owner ownerId) {
        this.id = id;
        this.customer = customerId;
        this.owner = ownerId;
    }
}
