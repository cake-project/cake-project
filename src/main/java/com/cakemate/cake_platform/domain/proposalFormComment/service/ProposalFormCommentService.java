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
import com.cakemate.cake_platform.domain.requestForm.exception.NotFoundProposalFormException;
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
            Long proposalFormId, Long memberId, String role
    ) {
        //데이터준비
        String content = commentCreateRequestDto.getContent();

        // 견적서 존재 여부 확인
        ProposalForm proposalForm = proposalFormRepository.findByIdAndIsDeletedFalse(proposalFormId)
                .orElseThrow(() -> new NotFoundProposalFormException("댓글을 달 견적서가 존재하지 않습니다."));

        //검증
        // 작성자 식별
        Customer customer = null;
        Owner owner = null;

        //토큰에서 꺼낸 role 값과 memberId로 누가 요청했는지 검증
        //이건 권한(role) 이 소비자 혹은 점주와 같으면
        //소비자 혹은 점주(memberId)가 실제 DB에 존재하는지 보고 같으면 통괴
        //그 외는 허용되지 않는 권한이니 예외
        if ("CUSTOMER".equals(role)) {
           customer = customerRepository.findByIdAndIsDeletedFalse(memberId)
                    .orElseThrow(() -> new  BadRequestException("유효한 고객 정보가 아닙니다."));
        } else if ("OWNER".equals(role)) {
          owner = ownerRepository.findByIdAndIsDeletedFalse(memberId)
                    .orElseThrow(() -> new BadRequestException("유효한 점주 정보가 아닙니다."));
        } else {
            throw new BadRequestException("지원하지 않는 권한입니다.");
        }

        // 만약 가져온 memberId가 customer,owner 둘 다 널이면 잘못된 사용자 요청이므로 예외 발생
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
                savedComment.getId() , proposalFormId, customerId, ownerId, content, savedComment.getCreatedAt()
        );

        return ApiResponse.success(
                HttpStatus.OK, "댓글 작성 성공", commentCreateResponseDto
        );
    }
}
