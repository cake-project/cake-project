package com.cakemate.cake_platform.domain.requestForm.entity;


import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Getter
@Entity
public class RequestForm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposalForm_id")
    private ProposalForm proposalForm;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm") // 문자열 방식으로 출력
    private LocalDateTime pickupDate;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RequestFormStatus status;

    @OneToOne(mappedBy = "requestForm", orphanRemoval = true)
    private Order order;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    protected RequestForm() {}

    public RequestForm(Long id, ProposalForm proposalForm, Customer customer, String title, String region,
                       String content, int desiredPrice, String image, LocalDateTime pickupDate,
                       RequestFormStatus status) {
        this.id = id;
        this.proposalForm = proposalForm;
        this.customer = customer;
        this.title = title;
        this.region = region;
        this.content = content;
        this.desiredPrice = desiredPrice;
        this.image = image;
        this.pickupDate = pickupDate;
        this.status = status;
    }
}
