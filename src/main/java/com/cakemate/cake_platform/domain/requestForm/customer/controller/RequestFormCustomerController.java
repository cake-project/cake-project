package com.cakemate.cake_platform.domain.requestForm.customer.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.request.CustomerRequestFormCreateRequestDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CustomerRequestFormCreateResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CustomerRequestFormGetDetailResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CustomerRequestFormGetListResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.service.RequestFormCustomerService;
import io.jsonwebtoken.Claims;
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
    @PostMapping("/customers")
    public ApiResponse<CustomerRequestFormCreateResponseDto>  createRequestForm(
            @RequestBody @Valid CustomerRequestFormCreateRequestDto requestFormCustomerRequestDto
    ) {
       return requestFormCustomerService.createRequestFormService(requestFormCustomerRequestDto);

    }
    /**
     *고객 의뢰 단건 조회 API
     */
    @GetMapping("/customers/{requestFormId}")
    public ApiResponse<CustomerRequestFormGetDetailResponseDto> getDetailRequestForm(
            @PathVariable("requestFormId") Long requestFormId
    ) {
        return requestFormCustomerService.getDetailRequestFormService(requestFormId);
    }
    /**
     * 고객 의뢰 다건 조회 API
     */
    @GetMapping("/customers")
    public ApiResponse<List<CustomerRequestFormGetListResponseDto>> getListRequestForm() {
        return requestFormCustomerService.getListRequestFormService();
    }
    /**
     * 고객 의뢰 삭제 API
     */
    @DeleteMapping("/customers/request-form/{requestFormId}")
    public ApiResponse<Object> deleteRequestForm(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable("requestFormId") Long requestFormId
    ) {
        String jwtToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(jwtToken);
        Long authenticatedCustomerId = jwtUtil.subjectMemberId(claims);

        return requestFormCustomerService.deleteListRequestFormService(
                requestFormId, authenticatedCustomerId
        );
    }


}
