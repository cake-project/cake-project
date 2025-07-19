package com.cakemate.cake_platform.domain.auth.entity;

import com.cakemate.cake_platform.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
@Entity
@Table(name = "customers")
public class Customer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String passwordConfirm;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "핸드폰 번호 형식을 지켜주세요(010-xxxx-xxxx)")
    private String phoneNumber;

    protected Customer() {
    }
    // 회원가입 생성자
    public Customer(String email, String password, String passwordConfirm, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
    // 로그인 생성자
    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
