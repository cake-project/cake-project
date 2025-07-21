package com.cakemate.cake_platform.domain.proposalForm.entity;

import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.auth.owner.entity.Owner;
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProposalFormStatus status;

    @Column(nullable = false)
    private Boolean isActive = true;

    //생성자
    protected ProposalForm() {}

    public ProposalForm(RequestForm requestForm, Store store, Owner owner, String title, String content,
                        ProposalFormStatus status) {
        this.requestForm = requestForm;
        this.store = store;
        this.owner = owner;
        this.title = title;
        this.content = content;
        this.status = status;
        this.isActive = true;
    }

    public ProposalForm(String title, String content, ProposalFormStatus status, RequestForm requestForm) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.requestForm = requestForm;
    }

    /**
     * proposalFormCreateRequestDto 정보를 꺼내와서 entity를 만들 떄 씁니다.
     */
    public ProposalForm(Long id, RequestForm requestForm, Store store, Owner owner, String title, String content,
                        ProposalFormStatus status) {
        this.id = id;
        this.requestForm = requestForm;
        this.store = store;
        this.owner = owner;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    //게터
    public Long getId() {
        return id;
    }

    public RequestForm getRequestForm() {
        return requestForm;
    }

    public Store getStore() {
        return store;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ProposalFormStatus getStatus() {
        return status;
    }

    public Boolean getActive() {
        return isActive;
    }
}
