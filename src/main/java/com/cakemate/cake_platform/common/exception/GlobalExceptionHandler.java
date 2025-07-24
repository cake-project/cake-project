package com.cakemate.cake_platform.common.exception;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.order.exception.MismatchedRequestAndProposalException;
import com.cakemate.cake_platform.domain.order.exception.UnauthorizedRequestFormAccessException;
import com.cakemate.cake_platform.domain.requestForm.exception.NotFoundProposalFormException;
import com.cakemate.cake_platform.domain.requestForm.exception.NotFoundRequestFormException;
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

    //lse : 의뢰서를 찾을 수 없을때 사용합니다.
    @ExceptionHandler(NotFoundRequestFormException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundRequestFormException(NotFoundRequestFormException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    //lse : 견적서를 찾을 수 없을때 사용합니다.
    @ExceptionHandler(NotFoundProposalFormException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundProposalFormException(NotFoundRequestFormException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 필수 경로 변수(PathVariable) 값이 없을 때 사용합니다.
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingPathVariable(MissingPathVariableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "필수 경로 변수가 없습니다."));
    }

    // 견적서를 찾을 수 없을 때 사용합니다.
    @ExceptionHandler(ProposalFormNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProposalFormNotFoundException(ProposalFormNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 의뢰서를 찾을 수 없을 때 사용합니다.
    @ExceptionHandler(RequestFormNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRequestFormNotFoundException(RequestFormNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 주문 생성에서 의뢰서와 견적서가 잘못 매칭되었을 때 사용합니다.
    @ExceptionHandler(MismatchedRequestAndProposalException.class)
    public ResponseEntity<ApiResponse<Void>> handleMismatchedRequestAndProposalException(MismatchedRequestAndProposalException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 본인의 의뢰서가 아닐 때 사용합니다.
    @ExceptionHandler(UnauthorizedRequestFormAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedRequestFormAccessException(UnauthorizedRequestFormAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    // 가게를 찾을 수 없을 때 사용합니다.
    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleStoreNotFoundException(StoreNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 주문을 찾을 수 없을 때 사용합니다.
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleOrderNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

}
