package com.cakemate.cake_platform.domain.member.customer.service;

import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.member.customer.dto.CustomerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerManagementService {

    private final MemberRepository memberRepository;
    private final CustomerRepository customerRepository;

    public CustomerManagementService(MemberRepository memberRepository, CustomerRepository customerRepository) {
        this.memberRepository = memberRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * 소비자 -> 내 정보 조회 Service
     * @param customerId
     * @return
     */
    public CustomerProfileResponseDto getCustomerProfileService(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (customer.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 회원입니다.");
        }

        CustomerProfileResponseDto responseDto = new CustomerProfileResponseDto(
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );

        return responseDto;
    }
}
