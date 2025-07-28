package com.cakemate.cake_platform.domain.auth.signin.customer.service;


import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.signin.customer.dto.response.CustomerSignInResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerSignInService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public CustomerSignInService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponse<CustomerSignInResponse> CustomerSignInProcess(SearchCommand signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다"));

        boolean isMatched = passwordEncoder.matches(password, customer.getPassword());
        if (!isMatched) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        Member customerInMember = memberRepository
                .findByCustomer_Email(email)
                .orElseThrow(() -> new RuntimeException("소비자 이메일이 존재하지 않습니다."));

        String customerJwtToken = jwtUtil.createMemberJwtToken(customerInMember);
        CustomerSignInResponse customerSignInResponse = new CustomerSignInResponse(customerJwtToken);

        // 차후 토큰 생성 예정
        ApiResponse<CustomerSignInResponse> SignInSuccess
                = ApiResponse
                .success(HttpStatus.OK, "환영합니다 " + customer.getName() + "님", customerSignInResponse);
        return SignInSuccess;
    }
}
