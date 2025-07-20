package com.cakemate.cake_platform.domain.member.repository;

import com.cakemate.cake_platform.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
