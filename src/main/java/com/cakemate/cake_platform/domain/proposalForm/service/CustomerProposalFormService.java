package com.cakemate.cake_platform.domain.proposalForm.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.exception.ProposalFormNotFoundException;
import com.cakemate.cake_platform.common.exception.UnauthorizedAccessException;
import com.cakemate.cake_platform.domain.proposalForm.dto.*;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.proposalForm.exception.InvalidProposalStatusException;
import com.cakemate.cake_platform.domain.proposalForm.exception.ProposalAlreadyAcceptedException;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.proposalFormComment.entity.ProposalFormComment;
import com.cakemate.cake_platform.domain.proposalFormComment.repository.ProposalFormCommentRepository;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CustomerProposalFormService {

    private final ProposalFormRepository proposalFormRepository;
    private final ProposalFormCommentRepository proposalFormCommentRepository;

    public CustomerProposalFormService(ProposalFormRepository proposalFormRepository,
                                       ProposalFormCommentRepository proposalFormCommentRepository) {
        this.proposalFormRepository = proposalFormRepository;
        this.proposalFormCommentRepository = proposalFormCommentRepository;
    }

    /**
     *customer 전용 견적서 상세 조회
     */

    @Transactional(readOnly = true)
    public ApiResponse<CustomerProposalFormDetailDto> getProposalFormDetailForCustomer(Long proposalFormId, Long customerId) {
        ProposalForm proposalForm = proposalFormRepository.findById(proposalFormId)
                .orElseThrow(() -> new ProposalFormNotFoundException("해당 견적서가 존재하지 않습니다."));

        RequestForm requestForm = proposalForm.getRequestForm();

        //검증 로직
        if (!requestForm.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedAccessException("조회 권한이 없습니다.");
        }

        // 의뢰서 DTO 만들기
        RequestFormDataDto requestFormDto = new RequestFormDataDto(
                requestForm.getId(),
                requestForm.getTitle(),
                requestForm.getRegion(),
                requestForm.getContent(),
                requestForm.getDesiredPrice(),
                requestForm.getImage(),
                requestForm.getDesiredPickupDate(),
                requestForm.getStatus().name(),
                requestForm.getCreatedAt()
        );

        //견적서 DTO 만들기
        CustomerProposalFormDataDto proposalFormDto = new CustomerProposalFormDataDto(
                proposalForm.getId(),
                proposalForm.getRequestForm().getId(),
                proposalForm.getStoreName(),
                proposalForm.getTitle(),
                proposalForm.getContent(),
                proposalForm.getProposedPrice(),
                proposalForm.getProposedPickupDate(),
                proposalForm.getCreatedAt(),
                proposalForm.getStatus().name(),
                proposalForm.getImage()
        );

        //댓글 리스트 DTO 만들기
        List<ProposalFormComment> comments = proposalFormCommentRepository.findByProposalForm(proposalForm);

        List<CommentDataDto> commentDataDtos = comments.stream()
                .map(comment -> {
                    Long writerCustomerId = null;
                    if (comment.getCustomer() != null) {
                        writerCustomerId = comment.getCustomer().getId();
                    }
                    Long ownerId = null;
                    if (comment.getOwner() != null) {
                        ownerId = comment.getOwner().getId();
                    }
                    return new CommentDataDto(
                            comment.getId(),
                            customerId,
                            ownerId,
                            comment.getContent(),
                            comment.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        CustomerProposalFormDetailDto responseDto = new CustomerProposalFormDetailDto(requestFormDto, proposalFormDto, commentDataDtos);

        return ApiResponse.success(HttpStatus.OK, "success", responseDto);
    }

    /**
     * 소비자 -> 견적서 선택 기능
     */
    @Transactional
    public CustomerProposalFormAcceptResponseDto acceptProposalFormByCustomer(Long proposalFormId, Long customerId, CustomerProposalFormAcceptRequestDto requestDto) {
        ProposalForm proposalForm = proposalFormRepository.findById(proposalFormId)
                .orElseThrow(() -> new ProposalFormNotFoundException("견적서가 존재하지 않습니다."));

        RequestForm requestForm = Objects.requireNonNull(proposalForm.getRequestForm(), "견적서에 연결된 의뢰서가 없습니다.");

        if (!requestForm.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedAccessException("본인의 의뢰서가 아닙니다.");
        }

        boolean alreadyAccepted = proposalFormRepository.existsByRequestFormIdAndStatus(requestForm.getId(), ProposalFormStatus.ACCEPTED);
        if (alreadyAccepted) {
            throw new ProposalAlreadyAcceptedException("이미 선택한 견적서가 있습니다.");
        }

        ProposalFormStatus proposalFormStatus;
        try {
            proposalFormStatus = requestDto.getProposalFormStatusEnum();
        } catch (IllegalArgumentException e) {
            throw new InvalidProposalStatusException("유효하지 않은 견적서 상태입니다.");
        }

        proposalForm.acceptStatus(proposalFormStatus);

        CustomerProposalFormAcceptResponseDto responseDto = new CustomerProposalFormAcceptResponseDto(proposalForm.getId(), proposalForm.getStatus());
        return responseDto;
    }
}
