package com.cakemate.cake_platform.common.exception;

import com.cakemate.cake_platform.common.dto.ApiResponse;
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
}
