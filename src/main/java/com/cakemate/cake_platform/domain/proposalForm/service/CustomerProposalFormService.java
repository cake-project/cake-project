package com.cakemate.cake_platform.domain.proposalForm.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.proposalForm.dto.CommentDataDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.CustomerProposalFormDetailDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.ProposalFormDataDto;
import com.cakemate.cake_platform.domain.proposalForm.dto.RequestFormDataDto;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.proposalFormComment.entity.ProposalFormComment;
import com.cakemate.cake_platform.domain.proposalFormComment.repository.ProposalFormCommentRepository;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .orElseThrow(() -> new RuntimeException("해당 견적서가 존재하지 않습니다."));

        RequestForm requestForm = proposalForm.getRequestForm();

        //검증 로직
        if (!requestForm.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("조회 권한이 없습니다.");
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
        ProposalFormDataDto proposalFormDto = new ProposalFormDataDto(
                proposalForm.getId(),
                proposalForm.getRequestForm().getId(),
                proposalForm.getStoreName(),
                proposalForm.getTitle(),
                proposalForm.getContent(),
                proposalForm.getManagerName(),
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
}
