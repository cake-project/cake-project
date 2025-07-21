package com.cakemate.cake_platform.domain.requestForm.customer.service;

import com.cakemate.cake_platform.domain.requestForm.customer.repository.RequestFormRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestFormCustomerService {

    private final RequestFormRepository requestFormCustomerRepository;

    public RequestFormCustomerService(RequestFormRepository requestFormCustomerRepository) {
        this.requestFormCustomerRepository = requestFormCustomerRepository;
    }
}
