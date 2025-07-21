package com.cakemate.cake_platform.domain.proposalForm.entity;

import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@Entity
public class ProposalForm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requestForm_id")
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

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm") // 문자열 방식으로 출력
    private LocalDateTime pickupDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProposalFormStatus status;

    protected ProposalForm() {}

    public ProposalForm(RequestForm requestForm, Store store, Owner owner, String title, String content,
                        int price, LocalDateTime pickupDate, ProposalFormStatus status) {
        this.requestForm = requestForm;
        this.store = store;
        this.owner = owner;
        this.title = title;
        this.content = content;
        this.price = price;
        this.pickupDate = pickupDate;
        this.status = status;

    }
}
