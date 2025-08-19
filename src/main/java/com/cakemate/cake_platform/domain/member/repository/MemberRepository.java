package com.cakemate.cake_platform.domain.member.repository;

import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.oAuthEnum.OAuthProvider;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByCustomer_Email(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String customerEmail);

    Optional<Member> findByOwner_Email(@Email(message = "email 형식을 지켜주십시오(ex. cake@gmail.com)") String ownerEmail);

    Optional<Member> findByOwnerId(Long ownerId);

    Optional<Member> findByOwner_NameAndOwner_PhoneNumberAndOwner_ProviderAndOwner_ProviderId(String ownerName,
                                                                                              String ownerPhoneNumber,
                                                                                              OAuthProvider ownerProvider,
                                                                                              Long ownerProviderId);

    default Optional<Member> findOwnerByKaKao(String ownerName, String ownerPhoneNumber,
                                              OAuthProvider ownerProvider, Long ownerProviderId) {
        return findByOwner_NameAndOwner_PhoneNumberAndOwner_ProviderAndOwner_ProviderId(ownerName, ownerPhoneNumber,
                ownerProvider, ownerProviderId);
    }

    Optional<Member> findByOwner_NameAndOwner_PhoneNumberAndOwner_Provider(String ownerName, String ownerPhoneNumber,
                                                                           OAuthProvider ownerProvider);

    default Optional<Member> findOwnerByProvider(String ownerName, String ownerPhoneNumber, OAuthProvider provider) {
        return findByOwner_NameAndOwner_PhoneNumberAndOwner_Provider(ownerName, ownerPhoneNumber, provider);
    }

    Optional<Member> findByCustomer_NameAndCustomer_PhoneNumberAndCustomer_ProviderAndCustomer_ProviderId(String customerName,
                                                                                                          String customerPhoneNumber,
                                                                                                          OAuthProvider customerProvider,
                                                                                                          Long customerProviderId);

    default Optional<Member> findByCustomerByKakao(String customerName, String customerPhoneNumber,
                                                   OAuthProvider customerProvider, Long customerProviderId) {
        return findByCustomer_NameAndCustomer_PhoneNumberAndCustomer_ProviderAndCustomer_ProviderId(customerName,
                customerPhoneNumber, customerProvider, customerProviderId);
    }

    Optional<Member> findByCustomer_NameAndCustomer_PhoneNumberAndCustomer_Provider(String customerName,
                                                                                    String customerPhoneNumber,
                                                                                    OAuthProvider customerProvider);

    default Optional<Member> findCustomerByProvider(String customerName, String customerPhoneNumber, OAuthProvider customerProvider) {
        return findByCustomer_NameAndCustomer_PhoneNumberAndCustomer_Provider(customerName, customerPhoneNumber, customerProvider);
    }


    Optional<Member> findByCustomer_PhoneNumber(String customerPhoneNumber);

    // 삭제되지 않은 이메일 조회
//    @Query("select m from Member m where m.email = :email and m.isDeleted = false")
//    Optional<Member> findActiveByEmail(@Param("email") String email);
}
