package com.cakemate.cake_platform.domain.member.owner.service;

import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class OwnerManagementService {

    private final MemberRepository memberRepository;

    public OwnerManagementService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
