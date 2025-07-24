package com.cakemate.cake_platform.domain.auth.entity;

import com.cakemate.cake_platform.common.entity.BaseTimeEntity;
import com.cakemate.cake_platform.domain.auth.exception.BadRequestException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
@Entity
@Table(name = "owners")
public class Owner extends BaseTimeEntity {

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

    @Column(nullable = false)
    private boolean isDeleted = false;

    /**
     * 아래는 JPA 애서 쓰는 기본 생성자 입니다.
     */
    protected Owner() {
    }

    // 회원가입 생성자
    public Owner(String email, String password, String passwordConfirm, String name, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // 로그인 생성자
    public Owner(String email, String password) {
        this.email = email;
        this.password = password;
    }


    /**
     * 아래는 회원 수정 생성자 입니다.
     */
    public Owner(String password, String name, String phoneNumber) {
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    //기능

    /**
     * 아래는 (점주)비밀번호 수정 시 쓰는 메소드 입니다.
     */
    public Owner changePassword(String encodedPassword) {
        this.password = encodedPassword;

        return this;

    }
    /**
     * 아래는 (점주) 이름 & 전화번호 수정 시 쓰는 메소드 입니다.
     */
    public Owner updateProfile(String newName, String newPhoneNumber) {
        // null 이 아니고 빈 문자열이 아닐 때만 저장
        if (newName == null || newName.isBlank()) {
            throw new BadRequestException("이름은 빈 문자열일 수 없습니다.");
        }
        if (newPhoneNumber == null || newPhoneNumber.isBlank()) {
            throw new BadRequestException("전화번호는 null 이거나 빈 문자열일 수 없습니다.");
        }
        this.name = newName;
        this.phoneNumber = newPhoneNumber;

        return this;
    }


}
