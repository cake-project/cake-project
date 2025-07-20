package com.cakemate.cake_platform.domain.auth.signin.owner.service;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OwnerSignInService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public OwnerSignInService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<Object> OwnerSignInProcess(SearchCommand signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다"));

        boolean isMatched = passwordEncoder.matches(password, owner.getPassword());

        if (!isMatched) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        // 차후 토큰 생성 예정
        ApiResponse<Object> SignInSuccess
                = ApiResponse.success(HttpStatus.OK, "환영합니다 " + owner.getName() + "님", null);
        return SignInSuccess;
    }
}
