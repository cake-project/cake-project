package com.cakemate.cake_platform.domain.auth.signin.customer.service;


import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerSignInService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerSignInService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<Object> CustomerSignInProcess(SearchCommand signInRequest) {
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다"));
        boolean isMatched = passwordEncoder.matches(password, customer.getPassword());
        if (!isMatched) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        ApiResponse<Object> SignInSuccess
                = ApiResponse.success(HttpStatus.OK, "환영합니다 " + customer.getName() + "님", null);
        return SignInSuccess;
    }
}
