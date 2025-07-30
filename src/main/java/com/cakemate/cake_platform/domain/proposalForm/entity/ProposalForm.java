package com.cakemate.cake_platform.domain.proposalForm.entity;

import com.cakemate.cake_platform.common.commonEnum.CakeSize;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.proposalForm.exception.InvalidProposalStatusException;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "proposal_forms")
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
    @Enumerated(EnumType.STRING)
    private CakeSize cakeSize;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int proposedPrice;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false)
    private LocalDateTime proposedPickupDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProposalFormStatus status = ProposalFormStatus.AWAITING;  // 디폴트 값;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected ProposalForm() {
    }

    public ProposalForm(RequestForm requestForm, Store store, Owner owner, String storeName, String title, CakeSize cakeSize, int quantity, String content,
                        String managerName, int proposedPrice, LocalDateTime proposedPickupDate, String image, ProposalFormStatus status) {
        this.requestForm = requestForm;
        this.store = store;
        this.owner = owner;
        this.storeName = storeName;
        this.title = title;
        this.cakeSize = cakeSize;
        this.quantity = quantity;
        this.content = content;
        this.managerName = managerName;
        this.proposedPrice = proposedPrice;
        this.proposedPickupDate = proposedPickupDate;
        this.image = image;
        this.status = status;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    //기능
    public void update(String title, CakeSize cakeSize, int quantity, String content, String managerName, int price, LocalDateTime pickupDate, String image) {
        this.title = title;
        this.cakeSize = cakeSize;
        this.quantity = quantity;
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

    public void confirmStatus(ProposalFormStatus proposalFormStatus) {
        if (proposalFormStatus != ProposalFormStatus.CONFIRMED) {
            throw new InvalidProposalStatusException("견적서 상태는 CONFIRMED로만 변경 가능합니다.");
        }

        if (this.status != ProposalFormStatus.ACCEPTED) {
            throw new InvalidProposalStatusException("점주는 ACCEPTED 상태에서만 CONFIRMED로만 변경할 수 있습니다.");
        }

        this.status = ProposalFormStatus.CONFIRMED;
    }

}
