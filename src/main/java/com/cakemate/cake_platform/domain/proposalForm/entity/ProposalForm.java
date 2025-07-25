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
    private String storeName;

    @Column(name = "manager_name")
    private String managerName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int proposedPrice;

    private String image;

    @Column(nullable = false)
    private LocalDateTime proposedPickupDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProposalFormStatus status = ProposalFormStatus.AWAITING;  // 디폴트 값;

    @Column(nullable = false)
    private boolean isDeleted = false;

    //생성자
    public ProposalForm() {}

    public ProposalForm(RequestForm requestForm, Store store, Owner owner, String storeName, String title, String content,
                        int proposedPrice, LocalDateTime proposedPickupDate, String image, ProposalFormStatus status) {
        this.requestForm = requestForm;
        this.store = store;
        this.owner = owner;
        this.storeName = storeName;
        this.title = title;
        this.content = content;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.image = image;
        this.status = status;
    }

    //기능
    public void update(String storeName, String title, String content, String managerName, int price, LocalDateTime pickupDate, String image) {
        this.storeName = storeName;
        this.title = title;
        this.content = content;
        this.managerName = managerName;
        this.proposedPrice = price;
        this.proposedPickupDate = pickupDate;
        this.image = image;
    }

    public void updateStatus(ProposalFormStatus status) {
        this.status = status;
    }

    public void delete() {
        this.isDeleted = true;
    }

}
