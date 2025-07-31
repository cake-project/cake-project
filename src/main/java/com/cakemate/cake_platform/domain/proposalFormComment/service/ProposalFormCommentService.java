package com.cakemate.cake_platform.domain.proposalFormComment.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.exception.MemberAlreadyDeletedException;
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

        Owner owner = null;
        Customer customer = null;
        boolean hasOwnerId = hasOwnerId(bearerJwtToken);
        boolean hasCustomerId = hasCustomerId(bearerJwtToken);
        if (hasOwnerId) {
            Long ownerId = jwtUtil.extractOwnerId(bearerJwtToken);
            owner = ownerRepository.findByIdAndIsDeletedFalse(ownerId).orElseThrow(() -> new RuntimeException());
            String content = commentCreateRequestDto.getContent();


            ProposalForm proposalForm = proposalFormRepository.findByIdAndIsDeletedFalse(proposalFormId)
                    .orElseThrow(() -> new MemberAlreadyDeletedException("댓글을 달 견적서가 존재하지 않습니다."));

            ProposalFormComment proposalFormComment = ProposalFormComment.create(proposalForm, customer, owner, content);

            ProposalFormComment savedComment = proposalFormCommentRepository.save(proposalFormComment);

            Long commentOwnerId = savedComment.getOwner().getId();

            CommentCreateResponseDto ownerCommentResponseDto
                    = new CommentCreateResponseDto().createOwnerCommentResponseDto(savedComment.getId(), proposalFormId, commentOwnerId, content);

            return ApiResponse.success(
                    HttpStatus.OK, "댓글 작성 성공", ownerCommentResponseDto
            );
        }
        if (hasCustomerId) {
            Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);
            customer = customerRepository.findByIdAndIsDeletedFalse(customerId).orElseThrow(() -> new RuntimeException());

        }
        String content = commentCreateRequestDto.getContent();

        ProposalForm proposalForm = proposalFormRepository.findByIdAndIsDeletedFalse(proposalFormId)
                .orElseThrow(() -> new MemberAlreadyDeletedException("댓글을 달 견적서가 존재하지 않습니다."));

        ProposalFormComment proposalFormComment = ProposalFormComment.create(proposalForm, customer, owner, content);

        ProposalFormComment savedComment = proposalFormCommentRepository.save(proposalFormComment);

        Long commentCustomerId = savedComment.getCustomer().getId();

        CommentCreateResponseDto customerCommentResponseDto
                = new CommentCreateResponseDto().createCustomerCommentResponseDto(savedComment.getId(), proposalFormId, commentCustomerId, content);

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
