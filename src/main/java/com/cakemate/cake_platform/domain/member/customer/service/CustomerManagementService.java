package com.cakemate.cake_platform.domain.member.customer.service;

import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerManagementService {

    private final MemberRepository memberRepository;

    public CustomerManagementService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
