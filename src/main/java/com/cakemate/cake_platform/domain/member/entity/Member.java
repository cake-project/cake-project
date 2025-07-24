package com.cakemate.cake_platform.domain.member.entity;

import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(nullable = false)
    private boolean isDeleted = false;

    protected Member() {
    }

    public Member(Customer customer) {
        this.customer = customer;
    }

    public Member(Owner ownerId) {
        this.owner = ownerId;
    }


}
