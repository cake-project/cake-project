package com.cakemate.cake_platform.domain.auth.signup.customer.repository;


import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.oAuthEnum.OAuthProvider;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String email);
    boolean existsByEmail(String email);
    boolean existsByNameAndPhoneNumber(String name, String phoneNumber);
    Optional<Customer> findByNameAndPhoneNumber(String name, String phoneNumber);

    Optional<Customer> findByNameAndPhoneNumberAndProviderAndProviderId(String name, String phoneNumber, OAuthProvider provider, Long providerId);

    //삭제 되지 않은 소비자 식별자를 조회할 떄 사용합니다.
    Optional<Customer> findByIdAndIsDeletedFalse(Long id);

}
