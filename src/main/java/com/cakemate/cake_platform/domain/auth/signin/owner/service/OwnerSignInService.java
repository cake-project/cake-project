package com.cakemate.cake_platform.domain.auth.signin.owner.service;

import com.cakemate.cake_platform.common.command.SearchCommand;

import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.exception.EmailNotFoundException;
import com.cakemate.cake_platform.domain.auth.exception.PasswordMismatchException;
import com.cakemate.cake_platform.domain.auth.signin.owner.dto.response.OwnerSignInResponse;
import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class OwnerSignInService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public OwnerSignInService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder,
                              MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse<OwnerSignInResponse> OwnerSignInProcess(SearchCommand signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("점주 이메일이 존재하지 않습니다."));

        boolean isMatched = passwordEncoder.matches(password, owner.getPassword());

        if (!isMatched) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        Member ownerInMember = memberRepository
                .findByOwner_Email(email)
                .orElseThrow(() -> new EmailNotFoundException("점주 이메일이 존재하지 않습니다."));

        String ownerJwtToken = jwtUtil.createMemberJwtToken(ownerInMember);

        OwnerSignInResponse ownerSignInResponse = new OwnerSignInResponse(ownerJwtToken);

        ApiResponse<OwnerSignInResponse> SignInSuccess
                = ApiResponse.success(HttpStatus.OK, "환영합니다 " + owner.getName() + "님", ownerSignInResponse);
        return SignInSuccess;
    }
}
