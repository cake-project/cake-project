package com.cakemate.cake_platform.domain.member.repository;

import com.cakemate.cake_platform.domain.member.entity.Member;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByCustomer_Email(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String customerEmail);
    Optional<Member> findByOwner_Email(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String ownerEmail);



}
