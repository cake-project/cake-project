package com.cakemate.cake_platform.domain.store.entity;

import com.cakemate.cake_platform.common.entity.BaseTimeEntity;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.store.owner.dto.StoreUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "stores")
public class Store extends BaseTimeEntity {
    /**
     * 컬럼 - 연관관계 컬럼을 제외한 컬럼을 정의합니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    // 가게 상호명
    @Column(nullable = false)
    private String businessName;

    // 가게 이름
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(updatable = false, unique = true)
    private String businessNumber;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private boolean isDeleted = false;


    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    protected Store() {
    }

    public Store(Owner owner, String businessName, String name, String address,
                 String businessNumber, String phoneNumber, String image) {
        this.businessName = businessName;
        this.owner = owner;
        this.name = name;
        this.address = address;
        this.businessNumber = businessNumber;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.isActive = false;
    }
    //가게 수정 서비스에서 사용
    public void update(StoreUpdateRequestDto dto) {
        if (dto.getName() != null) this.name = dto.getName();
        if (dto.getAddress() != null) this.address = dto.getAddress();
        if (dto.getPhoneNumber() != null) this.phoneNumber = dto.getPhoneNumber();
        if (dto.getImage() != null) this.image = dto.getImage();
        if (dto.getIsActive() != null) this.isActive = dto.getIsActive();
    }
    //가게 삭제 서비스에 사용
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
}
