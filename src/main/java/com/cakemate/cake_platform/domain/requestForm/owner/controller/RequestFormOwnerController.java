package com.cakemate.cake_platform.domain.requestForm.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.requestForm.owner.dto.RequestFormDetailOwnerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.owner.dto.RequestFormPageOwnerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.owner.service.RequestFormOwnerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RequestFormOwnerController {

    private final RequestFormOwnerService requestFormOwnerService;
    private final JwtUtil jwtUtil;

    public RequestFormOwnerController(RequestFormOwnerService requestFormOwnerService, JwtUtil jwtUtil) {
        this.requestFormOwnerService = requestFormOwnerService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 점주 -> 의뢰서 단건 조회 API
     *
     * @param bearerJwtToken
     * @param requestFormId
     * @return
     */
    @GetMapping("/owners/request-forms/{requestFormId}")
    public ApiResponse<RequestFormDetailOwnerResponseDto> getRequestDetailOwnerAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @PathVariable Long requestFormId
    ) {
        Long ownerId = jwtUtil.extractOwnerId(bearerJwtToken);

        RequestFormDetailOwnerResponseDto responseDto = requestFormOwnerService.getRequestDetailOwnerService(ownerId, requestFormId);
        ApiResponse<RequestFormDetailOwnerResponseDto> response = ApiResponse.success(HttpStatus.OK, "의뢰서 단건 조회가 완료되었습니다.", responseDto);

        return response;
    }

    /**
     * 점주 -> 의뢰서 목록 조회 API
     *
     * @param bearerJwtToken
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/owners/request-forms")
    public ApiResponse<RequestFormPageOwnerResponseDto> getRequestListOwnerAPI(
            @RequestHeader("Authorization") String bearerJwtToken,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long ownerId = jwtUtil.extractOwnerId(bearerJwtToken);

        int adjustedPage = Math.max(page - 1, 0);

        Pageable pageable = PageRequest.of(adjustedPage, size);

        RequestFormPageOwnerResponseDto<RequestFormDetailOwnerResponseDto> responseDto = requestFormOwnerService.getRequestListOwnerService(ownerId, pageable);
        ApiResponse<RequestFormPageOwnerResponseDto> response = ApiResponse.success(HttpStatus.OK, "의뢰서 목록 조회가 완료되었습니다.", responseDto);
        return response;
    }
}
