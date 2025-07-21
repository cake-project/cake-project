package com.cakemate.cake_platform.domain.auth.signup.owner.service;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.signup.owner.dto.response.OwnerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public OwnerService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder, MemberRepository memberRepository) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    public ApiResponse<OwnerSignUpResponse> ownerSaveProcess(SearchCommand ownerSignUpRequest) {
        String email = ownerSignUpRequest.getEmail();
        String password = ownerSignUpRequest.getPassword();
        String passwordConfirm = ownerSignUpRequest.getPasswordConfirm();
        String name = ownerSignUpRequest.getName();
        String phoneNumber = ownerSignUpRequest.getPhoneNumber();
        // 비밀번호 정규식 검증
        ownerSignUpRequest.hasPasswordPattern();
        // 비밀번호, 비밀빈호 확인이 일치하는지
        ownerSignUpRequest.isMatchedPassword();

        boolean existsByOwnerEmail = ownerRepository.existsByEmail(email);
        if (existsByOwnerEmail) {
            throw new IllegalArgumentException("이미 등록된 이메일 입니다");
        }

        String passwordEncode = passwordEncoder.encode(password);

        Owner ownerInfo = new Owner(email, passwordEncode, passwordConfirm, name, phoneNumber);
        Owner ownerInfoSave = ownerRepository.save(ownerInfo);
        // 멤버 테이블에 저장
        Long ownerId = ownerInfo.getId();
        Owner owner = ownerRepository
                .findById(ownerId)
                .orElseThrow(() -> new RuntimeException("OwnerId가 존재하지 않습니다"));
        Member ownerMember = new Member(owner);
        memberRepository.save(ownerMember);

        OwnerSignUpResponse ownerSignUpResponse = new OwnerSignUpResponse(ownerInfoSave);

        ApiResponse<OwnerSignUpResponse> signUpSuccess
                = ApiResponse
                .success(HttpStatus.CREATED,
                        ownerInfoSave.getName() + "님 회원가입이 완료되었습니다.",
                        ownerSignUpResponse);
        return signUpSuccess;

    }
}
