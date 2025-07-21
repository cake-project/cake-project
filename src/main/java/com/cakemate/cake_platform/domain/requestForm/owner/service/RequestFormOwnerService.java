package com.cakemate.cake_platform.domain.requestForm.owner.service;

import com.cakemate.cake_platform.domain.requestForm.repository.RequestFormRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestFormOwnerService {

    private final RequestFormRepository requestFormRepository;

    public RequestFormOwnerService(RequestFormRepository requestFormRepository) {
        this.requestFormRepository = requestFormRepository;
    }
}
