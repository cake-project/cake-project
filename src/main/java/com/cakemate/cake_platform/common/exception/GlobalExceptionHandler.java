package com.cakemate.cake_platform.common.exception;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.exception.*;
import com.cakemate.cake_platform.domain.order.owner.exception.InvalidOrderStatusException;
import com.cakemate.cake_platform.domain.proposalForm.exception.*;
import com.cakemate.cake_platform.domain.requestForm.exception.RequestFormAccessDeniedException;
import com.cakemate.cake_platform.domain.order.customer.exception.MismatchedRequestAndProposalException;
import com.cakemate.cake_platform.domain.order.customer.exception.UnauthorizedRequestFormAccessException;
import com.cakemate.cake_platform.domain.requestForm.exception.RequestFormDeletionNotAllowedException;
import com.cakemate.cake_platform.domain.store.owner.exception.*;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundCustomerException;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundOwnerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //점주가 없을때 사용
    @ExceptionHandler(NotFoundOwnerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundOwnerException(NotFoundOwnerException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
    //가게가 없을때 사용
    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleStoreNotFoundException(StoreNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }
    //가게 중복 등록시 사용
    @ExceptionHandler(DuplicatedStoreException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicatedStoreException(DuplicatedStoreException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT, ex.getMessage()));
    }
    //권한이 없을 떄 사용
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage()));
    }
    //필수값 데이터가 없을 때 사용
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiResponse<Void> response = ApiResponse.error(HttpStatus.BAD_REQUEST, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFoundCustomerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundCustomerException(NotFoundCustomerException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }


    // 필수 경로 변수(PathVariable) 값이 없을 때 사용합니다.
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingPathVariable(MissingPathVariableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
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
    // 견적서가 달린 의뢰서는 삭제할 수 없을 때 발생
    @ExceptionHandler(RequestFormDeletionNotAllowedException.class)
    public ResponseEntity<ApiResponse<Void>> handleRequestFormDeletionNotAllowed(
            RequestFormDeletionNotAllowedException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 주문을 찾을 수 없을 때 사용합니다.
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleOrderNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateBusinessNumberException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateBusinessNumberException(DuplicateBusinessNumberException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT, ex.getMessage()));
    }

    //JwtUtil에서 사용
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbidden(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, e.getMessage()));
    }

    // 회원이 존재하지 않을 때 사용합니다.
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMemberNotFoundException(MemberNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 이미 탈퇴한 회원일 경우 사용합니다.
    @ExceptionHandler(MemberAlreadyDeletedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAlreadyDeletedException(MemberAlreadyDeletedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    // 소비자가 존재하지 않을 때 사용합니다.
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 점주가 존재하지 않을 때 사용합니다.
    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleOwnerNotFound(OwnerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 견적서가 이미 존재할 때 사용합니다.
    @ExceptionHandler(ProposalFormAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleProposalFormAlreadyExists(ProposalFormAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 견적서 삭제 권한이 없을 때 사용합니다.
    @ExceptionHandler(ProposalFormDeleteAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleDeleteAccessDenied(ProposalFormDeleteAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    // 견적서를 삭제할 수 없는 상태일 때 사용합니다.
    @ExceptionHandler(ProposalFormDeleteInvalidStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleDeleteInvalidStatus(ProposalFormDeleteInvalidStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 견적서 수정 권한이 없을 때 사용합니다.
    @ExceptionHandler(ProposalFormUpdateAccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUpdateAccessDenied(ProposalFormUpdateAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    // 견적서를 수정할 수 없는 상태일 때 사용합니다.
    @ExceptionHandler(ProposalFormUpdateInvalidStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleUpdateInvalidStatus(ProposalFormUpdateInvalidStatusException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 해당 정보가 없을 때 사용합니다.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(AlreadyDeletedStoreException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyDeletedStoreException(AlreadyDeletedStoreException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    // 접근 권한이 없는 경우 발생하는 예외입니다.
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    // 이메일이 존재하지 않을 때 사용합니다.
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailNotFoundException(EmailNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    // 비밀번호가 일치하지 않을 때 사용합니다.
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handlePasswordMismatchException(PasswordMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 이미 등록된 이메일일 경우 발생하는 예외입니다.
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(HttpStatus.CONFLICT, ex.getMessage()));
    }

    // 비밀번호 형식이 유효하지 않을 때 발생하는 예외입니다.
    @ExceptionHandler(InvalidPasswordFormatException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidPasswordFormatException(InvalidPasswordFormatException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // proposalForm - 픽업 날짜가 현재 시간보다 과거인 경우 발생하는 예외입니다.
    @ExceptionHandler(InvalidProposedPickupDateException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidProposedPickupDateException(InvalidProposedPickupDateException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // proposalForm - 가격이 마이너스(음수)인 경우 발생하는 예외입니다.
    @ExceptionHandler(InvalidProposedPriceException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidProposedPriceException(InvalidProposedPriceException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 주문 상태 변경이 유효하지 않을 때 발생하는 예외입니다.
    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidOrderStatusException(InvalidOrderStatusException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 견적서 상태 변경이 유효하지 않을 때 발생하는 예외입니다.
    @ExceptionHandler(InvalidProposalStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidProposalStatusException(InvalidProposalStatusException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    // 요청 바디가 없거나 JSON 형식이 올바르지 않을 때 발생하는 예외를 처리합니다.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "요청 바디가 올바르지 않습니다."));
    }

    // 이미 소비자가 선택하여 ACCEPTED 상태인 견적서가 존재할 경우 발생합니다.
    @ExceptionHandler(ProposalAlreadyAcceptedException.class)
    public ResponseEntity<ApiResponse<Void>> handleProposalAlreadyAcceptedException(ProposalAlreadyAcceptedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}
