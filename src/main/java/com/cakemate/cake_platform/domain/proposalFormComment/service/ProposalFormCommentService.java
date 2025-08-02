package com.cakemate.cake_platform.domain.proposalFormComment.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.exception.MemberAlreadyDeletedException;
import com.cakemate.cake_platform.common.exception.UnauthorizedAccessException;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.request.CommentCreateRequestDto;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.response.CommentCreateResponseDto;
import com.cakemate.cake_platform.domain.proposalFormComment.entity.ProposalFormComment;

import com.cakemate.cake_platform.domain.proposalFormComment.repository.ProposalFormCommentRepository;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundCustomerException;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundOwnerException;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProposalFormCommentService {

    private final ProposalFormCommentRepository proposalFormCommentRepository;
    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final ProposalFormRepository proposalFormRepository;
    private final JwtUtil jwtUtil;


    public ProposalFormCommentService(ProposalFormCommentRepository proposalFormCommentRepository, CustomerRepository customerRepository, OwnerRepository ownerRepository, ProposalFormRepository proposalFormRepository, JwtUtil jwtUtil) {
        this.proposalFormCommentRepository = proposalFormCommentRepository;
        this.customerRepository = customerRepository;
        this.ownerRepository = ownerRepository;
        this.proposalFormRepository = proposalFormRepository;
        this.jwtUtil = jwtUtil;
    }


    /**
     * 특정 견적서에 대해 고객 또는 사장이 댓글을 작성하는 서비스
     */
    @Transactional
    public ApiResponse<CommentCreateResponseDto> createRequestFormCommentService(
            String bearerJwtToken,
            CommentCreateRequestDto commentCreateRequestDto,
            Long proposalFormId
    ) {
        // 1. 토큰 파싱
        String token = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(token);
        boolean isOwner = jwtUtil.isOwnerToken(claims);
        boolean isCustomer = jwtUtil.isCustomerToken(claims);

        if (!isOwner && !isCustomer) {
            throw new UnauthorizedAccessException("유효하지 않은 사용자 유형입니다.");
        }

        Long memberId = jwtUtil.extractMemberId(bearerJwtToken);

        Owner owner = null;
        Customer customer = null;
        boolean hasOwnerId = hasOwnerId(bearerJwtToken);
        boolean hasCustomerId = hasCustomerId(bearerJwtToken);
        if (hasOwnerId) {
            Long ownerId = jwtUtil.extractOwnerId(bearerJwtToken);
            owner = ownerRepository.findByIdAndIsDeletedFalse(ownerId).orElseThrow(() -> new NotFoundOwnerException("존재하지 않거나 탈퇴된 점주입니다."));
            String content = commentCreateRequestDto.getContent();


            ProposalForm proposalForm = proposalFormRepository.findByIdAndIsDeletedFalse(proposalFormId)
                    .orElseThrow(() -> new MemberAlreadyDeletedException("댓글을 달 견적서가 존재하지 않습니다."));

            // **권한 검증: 견적서 고객(의뢰자) 또는 점주만 댓글 가능**
            boolean isAuthorized = false;
            if (proposalForm.getRequestForm() != null
                    && proposalForm.getRequestForm().getCustomer().getId().equals(memberId)) {  // [수정] 고객 접근
                isAuthorized = true;
            }
            if (proposalForm.getOwner() != null
                    && proposalForm.getOwner().getId().equals(memberId)) {  // 점주 접근
                isAuthorized = true;
            }
            if (!isAuthorized) {
                throw new UnauthorizedAccessException("본인과 관련 없는 견적서에는 댓글을 작성할 수 없습니다.");
            }
            ProposalFormComment proposalFormComment = ProposalFormComment.create(proposalForm, customer, owner, content);

            ProposalFormComment savedComment = proposalFormCommentRepository.save(proposalFormComment);

            Long commentOwnerId = savedComment.getOwner().getId();

            CommentCreateResponseDto ownerCommentResponseDto
                    = new CommentCreateResponseDto().createOwnerCommentResponseDto(
                            savedComment.getId(), proposalFormId, commentOwnerId, content, savedComment.getCreatedAt()
            );

            return ApiResponse.success(
                    HttpStatus.OK, "댓글 작성 성공", ownerCommentResponseDto
            );
        }
        if (hasCustomerId) {
            Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);
            customer = customerRepository.findByIdAndIsDeletedFalse(customerId).orElseThrow(() -> new NotFoundCustomerException("존재하지 않거나 탈퇴된 소비자 입니다."));

        }
        String content = commentCreateRequestDto.getContent();

        ProposalForm proposalForm = proposalFormRepository.findByIdAndIsDeletedFalse(proposalFormId)
                .orElseThrow(() -> new MemberAlreadyDeletedException("댓글을 달 견적서가 존재하지 않습니다."));

        ProposalFormComment proposalFormComment = ProposalFormComment.create(proposalForm, customer, owner, content);

        ProposalFormComment savedComment = proposalFormCommentRepository.save(proposalFormComment);

        Long commentCustomerId = savedComment.getCustomer().getId();

        CommentCreateResponseDto customerCommentResponseDto
                = new CommentCreateResponseDto().createCustomerCommentResponseDto(
                        savedComment.getId(), proposalFormId, commentCustomerId, content, savedComment.getCreatedAt()
        );

        return ApiResponse.success(
                HttpStatus.OK, "댓글 작성 성공", customerCommentResponseDto
        );
    }
    private boolean hasOwnerId(String bearerJwtToken) {
        String substringToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(substringToken);
        boolean ownerToken = jwtUtil.isOwnerToken(claims);
        if (ownerToken) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasCustomerId(String bearerJwtToken) {
        String substringToken = jwtUtil.substringToken(bearerJwtToken);
        Claims claims = jwtUtil.verifyToken(substringToken);
        boolean customerType = jwtUtil.isCustomerToken(claims);
        if (customerType) {
            return true;
        } else {
            return false;
        }
    }
}
