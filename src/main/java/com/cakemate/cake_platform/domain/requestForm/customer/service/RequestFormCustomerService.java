package com.cakemate.cake_platform.domain.requestForm.customer.service;

import com.cakemate.cake_platform.domain.requestForm.repository.RequestFormRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestFormCustomerService {

    private final RequestFormRepository requestFormRepository;

    public RequestFormCustomerService(RequestFormRepository requestFormRepository) {
        this.requestFormRepository = requestFormRepository;

    }
}
