package com.cakemate.cake_platform.domain.auth.signup.customer.service;

import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;

import com.cakemate.cake_platform.common.exception.CustomerNotFoundException;
import com.cakemate.cake_platform.domain.auth.exception.EmailAlreadyExistsException;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.response.CustomerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class CustomerSignUpService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public CustomerSignUpService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, MemberRepository memberRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    public ApiResponse<CustomerSignUpResponse> customerSaveProcess(SearchCommand customerSignUpRequest) {
        String email = customerSignUpRequest.getEmail();
        String password = customerSignUpRequest.getPassword();
        String passwordConfirm = customerSignUpRequest.getPasswordConfirm();
        String name = customerSignUpRequest.getName();
        String phoneNumber = customerSignUpRequest.getPhoneNumber();
        // 비밀번호 정규식 검증
        customerSignUpRequest.hasPasswordPattern();
        // 비밀번호, 비밀빈호 확인이 일치하는지
        customerSignUpRequest.isMatchedPassword();

        boolean existsByCustomerEmail = customerRepository.existsByEmail(email);
        if (existsByCustomerEmail) {
            throw new EmailAlreadyExistsException("이미 등록된 이메일 입니다.");
        }
        String passwordEncode = passwordEncoder.encode(password);
        String passwordConfirmEncode = passwordEncoder.encode(passwordConfirm);

        Customer customerInfo = new Customer(email, passwordEncode, passwordConfirmEncode, name, phoneNumber);
        Customer customerSave = customerRepository.save(customerInfo);
        // Member 테이블에 저장
        Long customerId = customerInfo.getId();
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("CustomerId가 존재하지 않습니다."));
        Member custmerMember = new Member(customer);
        memberRepository.save(custmerMember);

        CustomerSignUpResponse customerSignUpResponse = new CustomerSignUpResponse(customerSave);

        ApiResponse<CustomerSignUpResponse> signUpSuccess
                = ApiResponse
                .success(HttpStatus.CREATED,
                        customerSave.getName() + "님 회원가입이 완료되었습니다.",
                        customerSignUpResponse);

        return signUpSuccess;
    }

}
