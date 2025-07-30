package com.cakemate.cake_platform.domain.requestForm.entity;

import com.cakemate.cake_platform.common.entity.BaseTimeEntity;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Timer;

@Getter
@Entity
@Table(name = "request_forms")
//Auditing 은 감사하는 것 -> 리퀘스트 폼이라는 엔티티가 변경되면 감지를 해서 디비에 적용을 시켜주는 어노테이션
//@EnableJpaAuditing 을 설정하먄 읽어 준다.-> 베이스 엔티티가 보는 상황(JpaConfig)
public class RequestForm {

    //속성

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int desiredPrice;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false)
    private LocalDateTime desiredPickupDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RequestFormStatus status = RequestFormStatus.REQUESTED; //디폴트 값

    //아래 추가함
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;



    /**
     * 아래는 JPA 에서 쓰는 기본 생성자 입니다.
     */
    protected RequestForm() {
    }

    //생성자

    //의뢰서 생성시에는 견적서가 없어도 생성 가능하니, proposalForm 을 뺀 생성자를 추가로 만들었습니다.
    public RequestForm(Customer customer, String title, String region,
                       String content, int desiredPrice, String image, LocalDateTime desiredPickupDate,
                       RequestFormStatus status) {
        this.customer = customer;
        this.title = title;
        this.region = region;
        this.content = content;
        this.desiredPrice = desiredPrice;
        this.image = image;
        this.desiredPickupDate = desiredPickupDate;
        this.status = status;
    }

    public void updateStatus(RequestFormStatus status) {
        this.status = status;
    }

    //가능
    public void softDelete() {
        this.isDeleted = true;
    }

    //견적서 최초 등록 시 - 의뢰서 상태 변경
    public void updateStatusToHasProposal() {
        this.status = RequestFormStatus.ESTIMATING;
    }
}
