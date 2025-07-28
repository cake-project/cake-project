package com.cakemate.cake_platform.domain.proposalFormComment.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.exception.MemberAlreadyDeletedException;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.exception.BadRequestException;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.request.CommentCreateRequestDto;
import com.cakemate.cake_platform.domain.proposalFormComment.dto.response.CommentCreateResponseDto;
import com.cakemate.cake_platform.domain.proposalFormComment.entity.ProposalFormComment;

import com.cakemate.cake_platform.domain.proposalFormComment.repository.ProposalFormCommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProposalFormCommentService {

    private final ProposalFormCommentRepository proposalFormCommentRepository;
    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final ProposalFormRepository proposalFormRepository;


    public ProposalFormCommentService(ProposalFormCommentRepository proposalFormCommentRepository, CustomerRepository customerRepository, OwnerRepository ownerRepository, ProposalFormRepository proposalFormRepository) {
        this.proposalFormCommentRepository = proposalFormCommentRepository;
        this.customerRepository = customerRepository;
        this.ownerRepository = ownerRepository;
        this.proposalFormRepository = proposalFormRepository;
    }


    /**
     * 특정 견적서에 대해 고객 또는 사장이 댓글을 작성하는 서비스
     */
    @Transactional
    public ApiResponse<CommentCreateResponseDto> createRequestFormCommentService(
            CommentCreateRequestDto commentCreateRequestDto,
            Long proposalFormId,
            Long memberId
    ) {
        //데이터준비
        String content = commentCreateRequestDto.getContent();

        // 견적서 존재 여부 확인
        ProposalForm proposalForm = proposalFormRepository.findByIdAndIsDeletedFalse(proposalFormId)
                .orElseThrow(() -> new MemberAlreadyDeletedException("댓글을 달 견적서가 존재하지 않습니다."));

        //검증
        //커스터머가 있으면 통과 그렇지 않으면(orElse) 널
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(memberId)
                .orElse(null);


        //오너가 있으면 통과 그렇지 않으면(orElse) 널
        Owner owner = ownerRepository.findByIdAndIsDeletedFalse(memberId)
                .orElse(null);

        // 만약 토큰에서 가져온 memberId가 customer,owner 둘 다 널이면 잘못된 사용자 요청이므로 예외 발생
        if (customer == null && owner == null) {
            throw new BadRequestException("유효한 사용자 정보가 아닙니다.");
        }
        //엔티티 만들기
        ProposalFormComment proposalFormComment = ProposalFormComment.create(proposalForm, customer, owner, content);


        //저장
        ProposalFormComment savedComment = proposalFormCommentRepository.save(proposalFormComment);

        // 저장된 엔티티에서 ID 불러오기
        //오너, 소비자가 널이 아니면 아이디를 불러오고 널이면 그대로 둔다.
        Long ownerId;
        if (savedComment.getOwner() != null) {
            ownerId = savedComment.getOwner().getId();
        } else {
            ownerId = null;
        }
        Long customerId;
        if (savedComment.getCustomer() != null) {
            customerId = savedComment.getCustomer().getId();
        } else {
            customerId = null;
        }

        //responseDto 만들기
        CommentCreateResponseDto commentCreateResponseDto
                = new CommentCreateResponseDto(
                savedComment.getId() , proposalFormId, customerId, ownerId, content
        );

        return ApiResponse.success(
                HttpStatus.OK, "댓글 작성 성공", commentCreateResponseDto
        );
    }
}
