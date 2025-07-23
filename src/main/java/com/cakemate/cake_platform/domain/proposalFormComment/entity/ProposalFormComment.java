package com.cakemate.cake_platform.domain.proposalFormComment.entity;

import com.cakemate.cake_platform.common.entity.BaseTimeEntity;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "proposalform_comments")
public class ProposalFormComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "proposalForm_id")
    private ProposalForm proposalForm;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(nullable = false)
    private String content;

    protected ProposalFormComment() {

    }


}
