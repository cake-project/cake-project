package com.cakemate.cake_platform.domain.requestForm.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.request.CustomerRequestFormCreateRequestDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CustomerRequestFormCreateResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CustomerRequestFormGetDetailResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CustomerRequestFormGetListResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.service.RequestFormCustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RequestFormCustomerController {
    //속
    private final RequestFormCustomerService requestFormCustomerService;
    private final JwtUtil jwtUtil;

    //생
    public RequestFormCustomerController(RequestFormCustomerService requestFormCustomerService, JwtUtil jwtUtil) {
        this.requestFormCustomerService = requestFormCustomerService;
        this.jwtUtil = jwtUtil;
    }

    //기
    /**
     *고객 의뢰 생성(등록) API
     */
    @PostMapping("/customers/request-forms")
    public ApiResponse<CustomerRequestFormCreateResponseDto>  createRequestForm(
            @RequestBody @Valid CustomerRequestFormCreateRequestDto requestFormCustomerRequestDto,
            @RequestHeader("Authorization") String bearerJwtToken
    ) {
        // 토큰 파싱 및 인증 처리
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);

        ApiResponse<CustomerRequestFormCreateResponseDto> requestFormService
                = requestFormCustomerService.createRequestFormService(
                requestFormCustomerRequestDto, customerId
        );
        return requestFormService;
    }

    /**
     *고객 의뢰 단건 조회 API
     */
    @GetMapping("/customers/request-forms/{requestFormId}")
    public ApiResponse<CustomerRequestFormGetDetailResponseDto> getDetailRequestForm(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable("requestFormId") Long requestFormId
    ) {

        // 토큰 파싱 및 인증 처리
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);
        return requestFormCustomerService.getDetailRequestFormService(
                requestFormId, customerId
        );
    }
    /**
     * 고객 의뢰 다건 조회 API
     */
    @GetMapping("/customers/request-forms")
    public ApiResponse<List<CustomerRequestFormGetListResponseDto>> getListRequestForm(
            @RequestHeader("Authorization") String bearerJwtToken
    ) {
        // 토큰 파싱 및 인증 처리
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);

        return requestFormCustomerService.getListRequestFormService( customerId );
    }
    /**
     * 고객 의뢰 삭제 API
     */
    @DeleteMapping("/customers/request-forms/{requestFormId}")
    public ApiResponse<Object> deleteRequestForm(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable("requestFormId") Long requestFormId
    ) {
        // 토큰 파싱 및 인증 처리
        Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);

        return requestFormCustomerService.deleteListRequestFormService(
                requestFormId, customerId
        );
    }


}
