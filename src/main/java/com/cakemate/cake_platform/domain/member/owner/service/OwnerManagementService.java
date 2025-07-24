package com.cakemate.cake_platform.domain.member.owner.service;

import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.member.owner.dto.request.UpdateOwnerProfileRequestDto;
import com.cakemate.cake_platform.domain.member.owner.dto.response.OwnerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundOwnerException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OwnerManagementService {

    private final MemberRepository memberRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;


    public OwnerManagementService(MemberRepository memberRepository, OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
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

    /**
     * (점주) 내 정보 수정 서비스
     */
    @Transactional
    public void putUpdateOwnerService(
            Long ownerId, UpdateOwnerProfileRequestDto updateOwnerProfileRequestDto
    ) {
        //데이터준비
        String password = updateOwnerProfileRequestDto.getPassword();
        String passwordConfirm = updateOwnerProfileRequestDto.getPasswordConfirm();
        String name = updateOwnerProfileRequestDto.getName();
        String phoneNumber = updateOwnerProfileRequestDto.getPhoneNumber();

        //점주 조회 & 예외처리
        Owner owner = ownerRepository.findByIdAndIsDeletedFalse(ownerId)
                .orElseThrow(() -> new NotFoundOwnerException("점주 정보를 찾을 수 없습니다."));

        //비밀번호 변경 검증 및 예외처리
        //비밀번호가 null 이 아니고, 비밀번호가 빈 문자열(password.isBlank)이면 안된다(!).
//        if (password != null && !password.isBlank()) {
//            owner.changePassword(passwordEncoder.encode(password));
//        } if () {
//
//        }



    }
}
