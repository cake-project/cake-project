package com.cakemate.cake_platform.domain.requestForm.owner.controller;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.requestForm.owner.dto.RequestFormDetailOwnerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.owner.dto.RequestFormPageOwnerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.owner.service.RequestFormOwnerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners")
public class RequestFormOwnerController {

    private final RequestFormOwnerService requestFormOwnerService;

    public RequestFormOwnerController(RequestFormOwnerService requestFormOwnerService) {
        this.requestFormOwnerService = requestFormOwnerService;
    }

    // 점주 -> 의뢰서 단건 조회 API
    @GetMapping("/{ownerId}/requestForms/{requestFormId}")
    public ApiResponse getRequestDetailOwnerAPI(
            @PathVariable Long ownerId,
            @PathVariable Long requestFormId
    ) {
        RequestFormDetailOwnerResponseDto responseDto = requestFormOwnerService.getRequestDetailOwnerService(ownerId, requestFormId);
        ApiResponse response = ApiResponse.success(HttpStatus.OK, "의뢰서 단건 조회가 완료되었습니다.", responseDto);

        return response;
    }

    // 점주 -> 의뢰서 목록 조회 API
    @GetMapping("/{ownerId}/requestForms")
    public ApiResponse getRequestListOwnerAPI(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        int adjustedPage = Math.max(page - 1, 0);

        Pageable pageable = PageRequest.of(adjustedPage, size);

        RequestFormPageOwnerResponseDto<RequestFormDetailOwnerResponseDto> responseDto = requestFormOwnerService.getRequestListOwnerService(ownerId, pageable);
        ApiResponse response = ApiResponse.success(HttpStatus.OK, "의뢰서 목록 조회가 완료되었습니다.", responseDto);
        return response;

    }
}
