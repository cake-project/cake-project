package com.cakemate.cake_platform.domain.requestForm.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.request.CreateRequestFormCustomerRequestDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CreateRequestFormCustomerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.GetDetailRequestFormCustomerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.service.RequestFormCustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<CreateRequestFormCustomerResponseDto>  createRequestForm(
            @RequestBody @Valid CreateRequestFormCustomerRequestDto requestFormCustomerRequestDto
    ) {
       return requestFormCustomerService.createRequestFormService(requestFormCustomerRequestDto);

    }
    /**
     *고객 의뢰 단건 조회 API
     */
    @GetMapping("/{requestFormId}")
    public ApiResponse<GetDetailRequestFormCustomerResponseDto> getDetailRequestForm(
            @PathVariable("requestFormId") Long requestFormId
    ) {
        return requestFormCustomerService.getDetailRequestFormService(requestFormId);
    }


}
