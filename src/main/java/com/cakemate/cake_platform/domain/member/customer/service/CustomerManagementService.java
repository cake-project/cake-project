package com.cakemate.cake_platform.domain.member.customer.service;

import com.cakemate.cake_platform.common.config.PasswordValidator;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.exception.MemberAlreadyDeletedException;
import com.cakemate.cake_platform.common.exception.MemberNotFoundException;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.exception.BadRequestException;
import com.cakemate.cake_platform.domain.auth.signup.customer.dto.response.CustomerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.member.customer.dto.reponse.CustomerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.customer.dto.reponse.UpdateCustomerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.customer.dto.request.UpdateCustomerProfileRequestDto;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundCustomerException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerManagementService {
    private final MemberRepository memberRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;


    public CustomerManagementService(MemberRepository memberRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, PasswordValidator passwordValidator) {
        this.memberRepository = memberRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
    }

    /**
     * 소비자 -> 내 정보 조회 Service
     * @param customerId
     * @return
     */
    public CustomerProfileResponseDto getCustomerProfileService(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        if (customer.isDeleted()) {
            throw new MemberAlreadyDeletedException("이미 탈퇴한 회원입니다.");
        }

        CustomerProfileResponseDto responseDto = new CustomerProfileResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );

        return responseDto;
    }
    /**
     * (소비자) 내 정보 수정 서비스
     */
    @Transactional
    public ApiResponse<UpdateCustomerProfileResponseDto> putUpdateCustomerService(
            Long customerId, UpdateCustomerProfileRequestDto dto
    ) {
        // 데이터 준비
        String password = dto.getPassword();
        String passwordConfirm = dto.getPasswordConfirm();
        String phoneNumber = dto.getPhoneNumber();

        // 소비자 조회 & 예외처리
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new NotFoundCustomerException("소비자 정보를 찾을 수 없습니다."));

//        // 비밀번호 변경
//        if (passwordValidator.isPasswordChangeRequested(password)) {
        //비밀번호와 비밀번호 확인값이 같은지 검사.
//            passwordValidator.validatePasswordMatch(password, passwordConfirm);
        // 암호화 후 엔티티에 반영
//            customer.changePassword(passwordEncoder.encode(password));
//        }

        // 4. 전화번호 검증
        if (phoneNumber == null || !phoneNumber.matches("^010-[0-9]{4}-[0-9]{4}$")) {
            throw new BadRequestException("핸드폰 번호 형식을 지켜주세요(010-xxxx-xxxx)");
        }

        // 5. 비밀번호 변경 검증 및 처리
        if (passwordValidator.isPasswordChangeRequested(password)) {
            if (passwordConfirm == null || !password.equals(passwordConfirm)) {
                throw new BadRequestException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            }
            customer.changePassword(passwordEncoder.encode(password));
        }

        // 이름 & 전화번호 업데이트
        Customer updatedCustomer = customer.updateProfile(phoneNumber);

        // 응답 DTO 생성
        UpdateCustomerProfileResponseDto responseDto = new UpdateCustomerProfileResponseDto(
                updatedCustomer.getId(),
                updatedCustomer.getEmail(),
                updatedCustomer.getName(),
                updatedCustomer.getPhoneNumber()
        );

        return ApiResponse.success(
                HttpStatus.OK, "회원 정보가 성공적으로 수정되었습니다.", responseDto
        );
    }

    /**
     * 소비자 회원 탈퇴
     */
    @Transactional
    public ApiResponse<Void> deleteCustomerProfileService(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        if (customer.isDeleted()) {
            throw new MemberAlreadyDeletedException("이미 탈퇴한 회원입니다.");
        }

        // soft delete 처리
        customer.delete();
        customerRepository.save(customer);

        CustomerProfileResponseDto responseDto = new CustomerProfileResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );

        return ApiResponse.success(HttpStatus.OK, customer.getName() + "님, 회원탈퇴가 정상적으로 완료되었습니다.", null);
    }
}
