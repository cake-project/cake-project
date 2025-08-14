package com.cakemate.cake_platform.domain.auth.signup.owner.repository;

import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.oAuthEnum.OAuthProvider;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsByEmail(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String email);
    Optional<Owner> findByEmail(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String email);
    boolean existsByNameAndPhoneNumber(String name, String phoneNumber);

    Optional<Owner> findByNameAndPhoneNumberAndProviderAndProviderId(String name, String phoneNumber, OAuthProvider provider, Long providerId);
    //삭제 되지 않은 점주 식별자를 조회할 떄 사용합니다.
    Optional<Owner> findByIdAndIsDeletedFalse(Long id);

    Optional<Owner> findByNameAndPhoneNumber(String name, String phoneNumber);

}