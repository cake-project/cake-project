package com.cakemate.cake_platform.domain.auth.signup.customer.service;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;

import com.cakemate.cake_platform.domain.auth.signup.customer.entity.Customer;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ApiResponse<Customer> customerSaveProcess(SearchCommand signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String passwordConfirm = signUpRequest.getPasswordConfirm();
        String name = signUpRequest.getName();
        String phoneNumber = signUpRequest.getPhoneNumber();

        String passwordEncode = passwordEncoder.encode(password);

        passwordEncoder.matches(password, passwordEncode);

        Customer customerInfo = new Customer(email, password, passwordConfirm, name, phoneNumber);

        Customer save = customerRepository.save(customerInfo);

        ApiResponse<Customer> signUpSuccess
                = ApiResponse.success(HttpStatus.CREATED, "회원가입이 완료되었습니다.", save);
        return signUpSuccess;
    }


}
