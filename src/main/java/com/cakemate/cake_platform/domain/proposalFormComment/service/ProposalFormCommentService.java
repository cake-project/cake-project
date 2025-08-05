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


import com.cakemate.cake_platform.domain.proposalFormComment.exception.UnauthorizedProposalCommentException;
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
     * 1. 토큰을 파싱해 사용자 유형(점주/고객) 판별
     * 2. 특정 댓글을 작성할 견적서 정보 조회
     * 3. 해당 견적서의 작성자(점주) 또는 의뢰자(고객)만 댓글을 달 수 있도록 권한 확인
     * 4. 댓글 저장 후 응답 반환
     */
    @Transactional
    public ApiResponse<CommentCreateResponseDto> createRequestFormCommentService(
            String bearerJwtToken,
            CommentCreateRequestDto commentCreateRequestDto,
            Long proposalFormId
    ) {
        // 1. 토큰 파싱 및 사용자 유형 확인
        // "Bearer {토큰}" 형태에서 "토큰" 부분만 잘라냄 (앞의 "Bearer " 제거)
        String token = jwtUtil.substringToken(bearerJwtToken);
        // 잘라낸 토큰이 유효한지 검증하고, 토큰 안의 내용(payload: 사용자 정보 등)을 Claims 객체로 가져옴
        Claims claims = jwtUtil.verifyToken(token);
        // 토큰 안의 정보로 "이 사용자가 점주(Owner)인지" 여부를 확인
        boolean isOwner = jwtUtil.isOwnerToken(claims);
        // 토큰 안의 정보로 "이 사용자가 고객(Customer)인지" 여부를 확인
        boolean isCustomer = jwtUtil.isCustomerToken(claims);

        // 유효한 사용자 유형이 아닐 경우 예외 발생
        if (!isOwner && !isCustomer) {
            throw new UnauthorizedProposalCommentException("유효하지 않은 사용자 유형입니다.");
        }

        // 토큰에서 memberId 추출
        Long memberId = jwtUtil.extractMemberId(bearerJwtToken);

        // 2. 점주인지 확인 후 로직 수행
        Owner owner = null;
        Customer customer = null;
        boolean hasOwnerId = hasOwnerId(bearerJwtToken);
        boolean hasCustomerId = hasCustomerId(bearerJwtToken);
        // 점주 정보 조회 (삭제된 계정은 예외)
        if (hasOwnerId) {
            Long ownerId = jwtUtil.extractOwnerId(bearerJwtToken);
            owner = ownerRepository.findByIdAndIsDeletedFalse(ownerId).orElseThrow(() -> new NotFoundOwnerException("존재하지 않거나 탈퇴된 점주입니다."));
            String content = commentCreateRequestDto.getContent();

            // 견적서 조회 (삭제된 경우 예외)
            ProposalForm proposalForm = proposalFormRepository.findByIdAndIsDeletedFalse(proposalFormId)
                    .orElseThrow(() -> new MemberAlreadyDeletedException("댓글을 달 견적서가 존재하지 않습니다."));

            //  권한 검증: 본인과 관련 있는 견적서만 댓글 가능
            boolean isAuthorized = false;
            if (proposalForm.getRequestForm() != null
                    && proposalForm.getRequestForm().getCustomer().getId().equals(memberId)) {  // 고객 접근
                isAuthorized = true;
            }
            if (proposalForm.getOwner() != null
                    && proposalForm.getOwner().getId().equals(memberId)) {  // 점주 접근
                isAuthorized = true;
            }
            if (!isAuthorized) {
                throw new UnauthorizedProposalCommentException("본인과 관련 없는 견적서에는 댓글을 작성할 수 없습니다.");
            }

            // 댓글 엔티티 생성 및 저장
            ProposalFormComment proposalFormComment = ProposalFormComment.create(proposalForm, customer, owner, content);
            ProposalFormComment savedComment = proposalFormCommentRepository.save(proposalFormComment);

            // 응답 DTO 생성
            Long commentOwnerId = savedComment.getOwner().getId();
            CommentCreateResponseDto ownerCommentResponseDto
                    = new CommentCreateResponseDto().createOwnerCommentResponseDto(
                            savedComment.getId(), proposalFormId, commentOwnerId, content, savedComment.getCreatedAt()
            );

            // 응답 반환
            return ApiResponse.success(
                    HttpStatus.OK, "댓글 작성 성공", ownerCommentResponseDto
            );
        }
        // 4. 고객(소비자)일 경우 로직 수행
        // 고객 정보 조회 (삭제된 계정은 예외)
        if (hasCustomerId) {
            Long customerId = jwtUtil.extractCustomerId(bearerJwtToken);
            customer = customerRepository.findByIdAndIsDeletedFalse(customerId).orElseThrow(() -> new NotFoundCustomerException("존재하지 않거나 탈퇴된 소비자 입니다."));

        }
        String content = commentCreateRequestDto.getContent();

        // 견적서 조회 (삭제된 경우 예외)
        ProposalForm proposalForm = proposalFormRepository.findByIdAndIsDeletedFalse(proposalFormId)
                .orElseThrow(() -> new MemberAlreadyDeletedException("댓글을 달 견적서가 존재하지 않습니다."));

        boolean isAuthorized = false;
        if (proposalForm.getRequestForm() != null
                && proposalForm.getRequestForm().getCustomer().getId().equals(memberId)) {  // 고객 접근
            isAuthorized = true;
        }
        if (proposalForm.getOwner() != null
                && proposalForm.getOwner().getId().equals(memberId)) {  // 점주 접근
            isAuthorized = true;
        }
        if (!isAuthorized) {
            throw new UnauthorizedProposalCommentException("본인과 관련 없는 견적서에는 댓글을 작성할 수 없습니다.");
        }

        // 댓글 생성 및 저장
        ProposalFormComment proposalFormComment = ProposalFormComment.create(proposalForm, customer, owner, content);
        ProposalFormComment savedComment = proposalFormCommentRepository.save(proposalFormComment);

        // 응답 DTO 생성
        Long commentCustomerId = savedComment.getCustomer().getId();
        CommentCreateResponseDto customerCommentResponseDto
                = new CommentCreateResponseDto().createCustomerCommentResponseDto(
                        savedComment.getId(), proposalFormId, commentCustomerId, content, savedComment.getCreatedAt()
        );

        // 응답 반환
        return ApiResponse.success(
                HttpStatus.OK, "댓글 작성 성공", customerCommentResponseDto
        );
    }

    /**
     * 토큰에서 점주 여부 확인
     */
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

    /**
     * 토큰에서 고객 여부 확인
     */
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
