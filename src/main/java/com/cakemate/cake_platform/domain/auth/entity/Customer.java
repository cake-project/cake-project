package com.cakemate.cake_platform.domain.auth.entity;

import com.cakemate.cake_platform.common.entity.BaseTimeEntity;
import com.cakemate.cake_platform.domain.auth.exception.BadRequestException;
import com.cakemate.cake_platform.domain.auth.oAuthEnum.OAuthProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
@Entity
@Table(name = "customers")
public class Customer extends BaseTimeEntity {

    //속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String customerKey;

    @Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)")
    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private String passwordConfirm;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "핸드폰 번호 형식을 지켜주세요(010-xxxx-xxxx)")
    private String phoneNumber;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider provider;

    private Long providerId;


    protected Customer() {
    }

    // 회원가입 생성자
    public Customer(String customerKey, String email, String password, String passwordConfirm, String name,
                    String phoneNumber, OAuthProvider provider, Long providerId) {
        this.customerKey = customerKey;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.provider = provider;
        this.providerId = providerId;
    }

    // 로그인 생성자
    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //기능

    // 비밀번호 변경
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    // 이름 & 전화번호 수정
    public Customer updateProfile(String newPhoneNumber) {

        if (newPhoneNumber == null || newPhoneNumber.isBlank()) {
            throw new BadRequestException("전화번호는 null 이거나 빈 문자열일 수 없습니다.");
        }

        this.phoneNumber = newPhoneNumber;
        return this;
    }

    //멤버(customer) 삭제 메서드
    public void delete() {
        this.isDeleted = true;
    }
}
