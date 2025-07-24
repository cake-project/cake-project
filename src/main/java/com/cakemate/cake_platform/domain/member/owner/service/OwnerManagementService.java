package com.cakemate.cake_platform.domain.member.owner.service;

import com.cakemate.cake_platform.common.config.PasswordValidator;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.exception.BadRequestException;
import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.member.owner.dto.request.UpdateOwnerProfileRequestDto;
import com.cakemate.cake_platform.domain.member.owner.dto.response.OwnerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.owner.dto.response.UpdateOwnerProfileResponseDto;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OwnerManagementService {

    private final MemberRepository memberRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidator passwordValidator;


    public OwnerManagementService(MemberRepository memberRepository, OwnerRepository ownerRepository, PasswordEncoder passwordEncoder, PasswordValidator passwordValidator) {
        this.memberRepository = memberRepository;
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidator = passwordValidator;
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
    public ApiResponse<UpdateOwnerProfileResponseDto> putUpdateOwnerService(
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


//        if (passwordValidator.isPasswordChangeRequested(password)) {
//            //비밀번호와 비밀번호 확인값이 같은지 검사.
//            passwordValidator.validatePasswordMatch(password, passwordConfirm);
//            // 암호화 후 엔티티에 반영
//            owner.changePassword(passwordEncoder.encode(password));
//        }
        // 이름 검증 → null 이거나 빈 문자열이면 예외
        if (name == null || name.isBlank()) {
            throw new BadRequestException("이름은 빈 문자열일 수 없습니다.");
        }

        // 전화번호 검증 → null 이거나 정규식 불일치면 예외
        if (phoneNumber == null || !phoneNumber.matches("^010-[0-9]{4}-[0-9]{4}$")) {
            throw new BadRequestException("핸드폰 번호 형식을 지켜주세요(010-xxxx-xxxx)");
        }

        // 비밀번호 변경 검증 및 처리 -> null 이거나 비밀번호 확인이 불일치면 예외
        if (passwordValidator.isPasswordChangeRequested(password)) {
            if (passwordConfirm == null || !password.equals(passwordConfirm)) {
                throw new BadRequestException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            }
            owner.changePassword(passwordEncoder.encode(password));
        }
        // 이름 & 전화번호 업데이트
        Owner updateOwnerProfile = owner.updateProfile(name, phoneNumber);
        Long foundOwnerId = updateOwnerProfile.getId();
        String updateOwnerProfileName = updateOwnerProfile.getName();
        String updateOwnerProfileEmail = updateOwnerProfile.getEmail();
        String updateOwnerProfilePhoneNumber = updateOwnerProfile.getPhoneNumber();


        UpdateOwnerProfileResponseDto updateOwnerProfileResponseDto = new UpdateOwnerProfileResponseDto(
                foundOwnerId, updateOwnerProfileName,
                updateOwnerProfileEmail, updateOwnerProfilePhoneNumber
        );

        return ApiResponse.success(
                HttpStatus.OK, "점주 정보가 성공적으로 수정되었습니다.", updateOwnerProfileResponseDto
                );
    }
}
