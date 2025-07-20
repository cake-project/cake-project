package com.cakemate.cake_platform.domain.requestForm.customer.repository;

import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestFormCustomerRepository extends JpaRepository<RequestForm, Long> {

}
