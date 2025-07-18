package com.cakemate.cake_platform.domain.requestForm.owner.service;

import com.cakemate.cake_platform.domain.requestForm.owner.repository.RequestFormOwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestFormOwnerService {

    private final RequestFormOwnerRepository requestFormOwnerRepository;

    public RequestFormOwnerService(RequestFormOwnerRepository requestFormOwnerRepository) {
        this.requestFormOwnerRepository = requestFormOwnerRepository;
    }
}
