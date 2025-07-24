package com.cakemate.cake_platform.common.exception;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.exception.BadRequestException;
import com.cakemate.cake_platform.domain.requestForm.exception.RequestFormAccessDeniedException;
import com.cakemate.cake_platform.domain.requestForm.exception.NotFoundProposalFormException;
import com.cakemate.cake_platform.domain.requestForm.exception.NotFoundRequestFormException;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundCustomerException;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundOwnerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundOwnerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundOwnerException(NotFoundOwnerException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(NotFoundCustomerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundCustomerException(NotFoundCustomerException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }


    //lse : 의뢰서를 찾을 수 없을때 사용합니다.
    @ExceptionHandler(NotFoundRequestFormException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundRequestFormException(NotFoundRequestFormException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    //lse : 견적서를 찾을 수 없을때 사용합니다.
    @ExceptionHandler(NotFoundProposalFormException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundProposalFormException(NotFoundProposalFormException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    //잘못된 요청 예외 처리 시 사용합니다.-> ex) 이름, 전화번호 null, 비밀번호 불일치 등
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }


    //의뢰서 접근 권한 예외 처리 시 사용합니다 -> 본인 소유가 아닌 의뢰서 삭제/수정 시
    @ExceptionHandler(RequestFormAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbiddenRequestForm(RequestFormAccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage()));
    }



}
