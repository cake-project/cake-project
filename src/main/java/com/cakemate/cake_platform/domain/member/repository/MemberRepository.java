package com.cakemate.cake_platform.domain.member.repository;

import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByCustomer_Email(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String customerEmail);
    Optional<Member> findByOwner_Email(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String ownerEmail);
    Optional<Member> findByOwnerId(Long ownerId);

    // 삭제되지 않은 이메일 조회
//    @Query("select m from Member m where m.email = :email and m.isDeleted = false")
//    Optional<Member> findActiveByEmail(@Param("email") String email);
}
