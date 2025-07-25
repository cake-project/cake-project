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

    @Column(nullable = false)
    private boolean isDeleted = false;


    /**
     * 아래는 JPA 에서 쓰는 기본 생성자 입니다.
     */
    protected ProposalFormComment() {

    }

    /**
     * 아래는 댓글 생성을 위한 메소드 입니다.
     */
    public static ProposalFormComment create(ProposalForm proposalForm, Customer customer, Owner owner, String content) {
        ProposalFormComment comment = new ProposalFormComment(); // 기본 생성자로 객체 만듦
        comment.proposalForm = proposalForm;  // 어떤 견적서에 달린 댓글인지 연결
        comment.customer = customer;          // 작성자가 고객이면 넣음
        comment.owner = owner;                // 작성자가 사장이면 넣음
        comment.content = content;            // 댓글 내용
        return comment;                       // 완성된 댓글 객체 반환
    }

}
