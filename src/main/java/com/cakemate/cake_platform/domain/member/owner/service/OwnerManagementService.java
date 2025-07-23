package com.cakemate.cake_platform.domain.member.owner.service;

import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.member.owner.dto.OwnerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class OwnerManagementService {

    private final MemberRepository memberRepository;
    private final OwnerRepository ownerRepository;

    public OwnerManagementService(MemberRepository memberRepository, OwnerRepository ownerRepository) {
        this.memberRepository = memberRepository;
        this.ownerRepository = ownerRepository;
    }

    public OwnerProfileResponseDto getOwnerProfileService(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (owner.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 회원입니다.");
        }

        OwnerProfileResponseDto responseDto = new OwnerProfileResponseDto(
                owner.getName(),
                owner.getEmail(),
                owner.getPhoneNumber()
        );

        return responseDto;
    }
}
