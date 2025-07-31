package com.cakemate.cake_platform.domain.proposalFormComment.entity;

import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@Entity
@Table(name = "proposal_form_comments")
public class ProposalFormComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "proposal_form_id")
    private ProposalForm proposalForm;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    /**
     * 아래는 JPA 에서 쓰는 기본 생성자 입니다.
     */
    protected ProposalFormComment() {

    }

    /**
     * 아래는 댓글 생성을 위한 메소드 입니다.
     */
    public static ProposalFormComment create(ProposalForm proposalForm, Customer customer, Owner owner, String content) {
        ProposalFormComment comment = new ProposalFormComment();
        comment.proposalForm = proposalForm;
        comment.customer = customer;
        comment.owner = owner;
        comment.content = content;
        comment.createdAt = LocalDateTime.now(ZoneOffset.UTC);
        return comment;
    }
}
