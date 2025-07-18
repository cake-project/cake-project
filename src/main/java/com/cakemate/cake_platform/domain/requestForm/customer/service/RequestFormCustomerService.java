package com.cakemate.cake_platform.domain.requestForm.customer.service;

import com.cakemate.cake_platform.domain.requestForm.customer.repository.RequestFormCustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestFormCustomerService {

    private final RequestFormCustomerRepository requestFormCustomerRepository;

    public RequestFormCustomerService(RequestFormCustomerRepository requestFormCustomerRepository) {
        this.requestFormCustomerRepository = requestFormCustomerRepository;
    }
}
