package com.cakemate.cake_platform.domain.requestForm.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.request.RequestFormCustomerRequestDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.RequestFormCustomerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.service.RequestFormCustomerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class RequestFormCustomerController {
    //속
    private final RequestFormCustomerService requestFormCustomerService;

    //생
    public RequestFormCustomerController(RequestFormCustomerService requestFormCustomerService) {
        this.requestFormCustomerService = requestFormCustomerService;
    }

    //기
    /**
     *고객 의뢰 생성(등록) API
     */
    @PostMapping
    public ApiResponse<RequestFormCustomerResponseDto>  createRequestForm(
            @RequestBody @Valid RequestFormCustomerRequestDto requestFormCustomerRequestDto
    ) {
       return requestFormCustomerService.createRequestFormService(requestFormCustomerRequestDto);

    }


}
