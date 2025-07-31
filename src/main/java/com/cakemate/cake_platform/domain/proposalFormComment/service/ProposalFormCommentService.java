package com.cakemate.cake_platform.domain.proposalFormComment.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.exception.MemberAlreadyDeletedException;
import com.cakemate.cake_platform.common.exception.UnauthorizedAccessException;
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


    public ProposalFormCommentService(ProposalFormCommentRepository proposalFormCommentRepository, CustomerRepository customerRepository, OwnerRepository ownerRepository, ProposalFormRepository proposalFormRepository) {
        this.proposalFormCommentRepository = proposalFormCommentRepository;
        this.customerRepository = customerRepository;
        this.ownerRepository = ownerRepository;
        this.proposalFormRepository = proposalFormRepository;
    }


    /**
     * 특정 견적서에 대해 고객 또는 사장이 댓글을 작성하는 서비스
     */

}
