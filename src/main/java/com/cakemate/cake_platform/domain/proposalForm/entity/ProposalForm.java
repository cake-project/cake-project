package com.cakemate.cake_platform.domain.proposalForm.entity;

import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ProposalForm {
    //속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_form_id")
    private RequestForm requestForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private int proposedPrice;

    private String image;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm") // 문자열 방식으로 출력
    private LocalDateTime proposedPickupDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProposalFormStatus status = ProposalFormStatus.AWAITING;  // 디폴트 값;

    @Column(nullable = false)
    private boolean isDeleted = false;

    protected ProposalForm() {
    }

    //아래 추가함
    public ProposalForm(RequestForm requestForm, Store store, Owner owner, String title, String content,
                        int proposedPrice, LocalDateTime proposedPickupDate, ProposalFormStatus status) {
        this.requestForm = requestForm;
        this.store = store;
        this.owner = owner;
        this.title = title;
        this.content = content;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.status = status;
    }
//    public ProposalForm(RequestForm requestForm, Store store, Owner owner, String title, String content,
//                        int proposalPrice, LocalDateTime pickupDate, ProposalFormStatus status) {
//        this.requestForm = requestForm;
//        this.store = store;
//        this.owner = owner;
//        this.title = title;
//        this.content = content;
//        this.status = status;
//    }

    public ProposalForm(String title, String content,
                        ProposalFormStatus status, RequestForm requestForm) {
        this.requestForm = requestForm;
        this.title = title;
        this.content = content;
        this.status = status;
    }
}
