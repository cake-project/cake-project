package com.cakemate.cake_platform.domain.proposalForm.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.exception.OwnerNotFoundException;
import com.cakemate.cake_platform.common.exception.ProposalFormNotFoundException;
import com.cakemate.cake_platform.common.exception.RequestFormNotFoundException;
import com.cakemate.cake_platform.common.exception.StoreNotFoundException;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import com.cakemate.cake_platform.domain.proposalForm.dto.*;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.proposalForm.exception.*;
import com.cakemate.cake_platform.domain.proposalForm.exception.ResourceNotFoundException;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.proposalFormComment.entity.ProposalFormComment;
import com.cakemate.cake_platform.domain.proposalFormComment.repository.ProposalFormCommentRepository;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.repository.RequestFormRepository;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.repository.StoreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProposalFormService {

    private final ProposalFormRepository proposalFormRepository;
    private final RequestFormRepository requestFormRepository;
    private final ProposalFormCommentRepository proposalFormCommentRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public ProposalFormService(ProposalFormRepository proposalFormRepository, RequestFormRepository requestFormRepository, ProposalFormCommentRepository proposalFormCommentRepository, StoreRepository storeRepository, MemberRepository memberRepository) {
        this.proposalFormRepository = proposalFormRepository;
        this.requestFormRepository = requestFormRepository;
        this.proposalFormCommentRepository = proposalFormCommentRepository;
        this.storeRepository = storeRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * proposalForm 생성 서비스
     */
    @Transactional
    public ApiResponse<ProposalFormDataDto> createProposal(Long ownerId, ProposalFormCreateRequestDto requestDto) {
        //데이터 준비
        //검증 로직(requestForm 존재 여부 확인)
        //Owner 조회 (만약 owner 엔티티가 없으면 Member 등으로 대체)
        Member member = memberRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException("해당 점주를 찾을 수 없습니다."));
        Owner owner = member.getOwner();
        if (owner == null) {
            throw new ResourceNotFoundException("해당하는 점주 정보가 없습니다.");
        }
        //Owner가 가진 Store 가져오기
        Store store = storeRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new StoreNotFoundException("해당 점주의 가게를 찾을 수 없습니다."));

        RequestForm requestForm = requestFormRepository.findById(requestDto.getRequestFormId())
                .orElseThrow(() -> new RequestFormNotFoundException("해당 의뢰서를 찾을 수 없습니다."));

        //검증 로직(견적서 중복 생성 방지)
        boolean exists = proposalFormRepository.existsByRequestForm(requestForm);
        if (exists) {
            throw new ProposalFormAlreadyExistsException("이미 이 의뢰서에 대한 견적서가 존재합니다.");
        }
        //엔티티 만들기
        ProposalForm proposalForm = new ProposalForm(
                requestForm,
                store,
                owner,
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getProposedPrice(),
                requestDto.getProposedPickupDate(),
                requestDto.getImage(),
                ProposalFormStatus.fromString(requestDto.getProposalFormStatus())
        );

        // 최초 견적서 등록 시 의뢰서 상태 변경
        long count = proposalFormRepository.countByRequestForm(requestForm);

        //저장
        ProposalForm savedProposalForm = proposalFormRepository.save(proposalForm);

        //DTO 만들기
        ProposalFormDataDto dataDto = new ProposalFormDataDto(
                savedProposalForm.getId(),
                savedProposalForm.getTitle(),
                savedProposalForm.getContent(),
                savedProposalForm.getManagerName(),
                savedProposalForm.getProposedPrice(),
                savedProposalForm.getProposedPickupDate(),
                savedProposalForm.getCreatedAt(),
                savedProposalForm.getStatus().name(),
                savedProposalForm.getImage()
        );

        //응답 DTO 만들기
        ApiResponse<ProposalFormDataDto> response = ApiResponse.success(HttpStatus.CREATED, "created", dataDto);
        return response;
    }

    /**
     * proposalForm 단건 상세 조회 서비스
     */
    @Transactional(readOnly = true)
    public ApiResponse<ProposalFormContainsRequestFormDataDto> getProposalFormDetail(Long proposalFormId) {
        //데이터 준비

        //조회
        ProposalForm foundProposalForm = proposalFormRepository.findById(proposalFormId)
                .orElseThrow(() -> new ProposalFormNotFoundException("해당 제안서가 존재하지 않습니다."));

        //RequestForm 조회
        RequestForm requestForm = foundProposalForm.getRequestForm();

        //comment List 조회
        List<ProposalFormComment> commentList = proposalFormCommentRepository.findByProposalForm_IdOrderByCreatedAtAsc(proposalFormId);

        //DTO 만들기(ProposalForm, RequestForm, Comment 데이터를 합쳐 DTO 생성)
        //ProposalFormDto 만들기
        ProposalFormDataDto proposalFormDataDto = new ProposalFormDataDto(
                foundProposalForm.getId(),
                foundProposalForm.getTitle(),
                foundProposalForm.getContent(),
                foundProposalForm.getManagerName(),
                foundProposalForm.getProposedPrice(),
                foundProposalForm.getProposedPickupDate(),
                foundProposalForm.getCreatedAt(),
                foundProposalForm.getStatus().name(),
                foundProposalForm.getImage()
        );

        //RequestFormDto 만들기
        RequestFormDataDto requestFormDataDto = new RequestFormDataDto(
                requestForm.getId(),
                requestForm.getTitle(),
                requestForm.getRegion(),
                requestForm.getDesiredPrice(),
                requestForm.getDesiredPickupDate(),
                requestForm.getStatus().name(),
                requestForm.getCreatedAt(),
                requestForm.getImage()
        );

        //commentFormDto 리스트 만들기
        List<CommentDataDto> commentDtoList = commentList.stream()
                .map(comment -> {
                    Long customerId = null;
                    //comment에 연결된 customer가 있으면 ID 추출
                    if (comment.getCustomer() != null) {
                        customerId = comment.getCustomer().getId();
                    }
                    //comment에 연결된 owner가 있으면 ID 추출
                    Long ownerId = null;
                    if (comment.getOwner() != null) {
                        ownerId = comment.getOwner().getId();
                    }
                    //CommentDataDto 생성 및 반환
                    return new CommentDataDto(
                            comment.getId(),
                            customerId,
                            ownerId,
                            comment.getContent(),
                            comment.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

        //응답 DTO 만들기
        ProposalFormContainsRequestFormDataDto responseDto = new ProposalFormContainsRequestFormDataDto(requestFormDataDto, proposalFormDataDto, commentDtoList);

        //반환
        ApiResponse<ProposalFormContainsRequestFormDataDto> response = ApiResponse.success(HttpStatus.OK, "success", responseDto);
        return response;
    }

    /**
     * proposalForm 목록 조회 서비스
     */
    @Transactional(readOnly = true)
    public ApiResponse<List<ProposalFormContainsRequestFormDataDto>> getProposalFormList() {
        //데이터 준비
        //조회
        List<ProposalForm> proposalFormList = proposalFormRepository.findAll();

        //DTO 만들기( ProposalForm과 RequestForm 데이터를 합쳐 DTO 생성)
        List<ProposalFormContainsRequestFormDataDto> dataList = proposalFormList.stream()
                .map(proposalForm -> {
                    RequestForm requestForm = proposalForm.getRequestForm();

                    ProposalFormDataDto proposalDto = new ProposalFormDataDto(
                            proposalForm.getId(),
                            proposalForm.getTitle(),
                            proposalForm.getContent(),
                            proposalForm.getManagerName(),
                            proposalForm.getProposedPrice(),
                            proposalForm.getProposedPickupDate(),
                            proposalForm.getCreatedAt(),
                            proposalForm.getStatus().name(),
                            proposalForm.getImage()
                    );

                    RequestFormDataDto requestDto = new RequestFormDataDto(
                            requestForm.getId(),
                            requestForm.getTitle(),
                            requestForm.getRegion(),
                            requestForm.getDesiredPrice(),
                            requestForm.getDesiredPickupDate(),
                            requestForm.getStatus().name(),
                            requestForm.getCreatedAt(),
                            requestForm.getImage()
                    );

                    return new ProposalFormContainsRequestFormDataDto(requestDto, proposalDto);
                })
                .collect(Collectors.toList());

        //응답 DTO 만들기
        ApiResponse<List<ProposalFormContainsRequestFormDataDto>> response = ApiResponse.success(
                HttpStatus.OK, "success", dataList);
        //반환
        return response;
    }

    /**
     * proposalForm 수정 서비스
     */
    @Transactional
    public ApiResponse<ProposalFormDataDto> updateProposalForm(Long proposalFormId, Long ownerId, ProposalFormUpdateRequestDto requestDto) {
        //데이터 준비
        //조회
        Member foundOwner = memberRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException("해당 점주를 찾을 수 없습니다."));
        ProposalForm foundProposalForm = proposalFormRepository.findById(proposalFormId)
                .orElseThrow(() -> new ProposalFormNotFoundException("해당 견적서가 존재하지 않습니다."));
        //검증 로직(해당 점주가 작성한 견적서인지 확인)
        if (!foundProposalForm.getOwner().getId().equals(foundOwner.getId())) {
            throw new ProposalFormUpdateAccessDeniedException("견적서에 대한 수정 권한이 없습니다.");
        }
        //status 검증(AWAITING일 때만 수정 가능)
        if (foundProposalForm.getStatus() != ProposalFormStatus.AWAITING) {
            throw new ProposalFormUpdateInvalidStatusException("AWAITING 상태일 때만 수정할 수 있습니다.");
        }

        //수정
        foundProposalForm.update(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getManagerName(),
                requestDto.getProposedPrice(),
                requestDto.getProposedPickupDate(),
                requestDto.getImage()
        );

        //저장
        ProposalForm updatedProposalForm = proposalFormRepository.save(foundProposalForm);

        //응답 DTO 만들기
        ProposalFormDataDto responseDto = new ProposalFormDataDto(
                updatedProposalForm.getId(),
                updatedProposalForm.getTitle(),
                updatedProposalForm.getContent(),
                updatedProposalForm.getManagerName(),
                updatedProposalForm.getProposedPrice(),
                updatedProposalForm.getProposedPickupDate(),
                updatedProposalForm.getCreatedAt(),
                updatedProposalForm.getStatus().name(),
                updatedProposalForm.getImage()
                );
        //응답 반환
        ApiResponse<ProposalFormDataDto> response = ApiResponse.success(HttpStatus.OK, "updated", responseDto);
        return response;
    }

    /**
     * proposalForm 삭제 서비스
     */
    @Transactional
    public ApiResponse<String> deleteProposalForm(Long proposalFormId, Long ownerId) {
        //조회
        Member foundOwner = memberRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("해당 점주를 찾을 수 없습니다."));

        ProposalForm foundProposalForm = proposalFormRepository.findById(proposalFormId)
                .orElseThrow(() -> new RuntimeException("해당 제안서가 존재하지 않습니다."));

        //권한 확인
        if (!foundProposalForm.getOwner().getId().equals(foundOwner.getId())) {
            throw new ProposalFormDeleteAccessDeniedException("삭제 권한이 없습니다.");
        }

        //status 확인 (예: AWAITING 상태만 삭제 가능하도록 제한)
        if (foundProposalForm.getStatus() != ProposalFormStatus.AWAITING) {
            throw new ProposalFormDeleteInvalidStatusException("AWAITING 상태만 삭제할 수 있습니다.");
        }

        //삭제(논리 삭제)
        foundProposalForm.delete();

        //응답 반환
        ApiResponse<String> response = ApiResponse.success(HttpStatus.OK, "deleted", null);
        return response;
    }
}