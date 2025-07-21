package com.cakemate.cake_platform.domain.proposalForm.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.proposalForm.dto.*;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.requestForm.customer.repository.RequestFormRepository;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProposalFormService {

    private final ProposalFormRepository proposalFormRepository;
    private final RequestFormRepository requestFormRepository;

    public ProposalFormService(ProposalFormRepository proposalFormRepository, RequestFormRepository requestFormRepository) {
        this.proposalFormRepository = proposalFormRepository;
        this.requestFormRepository = requestFormRepository;
    }

    /**
     * proposalForm 생성 서비스
     */
    @Transactional
    public ApiResponse<ProposalFormDataDto> createProposal(ProposalFormCreateRequestDto requestDto) {
        //데이터 준비
        String title = requestDto.getTitle();
        String content = requestDto.getContent();
        Long requestFormId = requestDto.getRequestFormId();
        String proposalFormStatus = requestDto.getProposalFormStatus();

        //검증 로직(requestForm 존재 여부 확인)
        RequestForm requestForm = requestFormRepository.findById(requestFormId)
                .orElseThrow(() -> new RuntimeException("해당 의뢰서를 찾을 수 없습니다."));

        //엔티티 만들기
        ProposalForm proposalForm = new ProposalForm(
                title,
                content,
                ProposalFormStatus.fromString(proposalFormStatus),
                requestForm
        );

        //저장
        ProposalForm savedProposalForm = proposalFormRepository.save(proposalForm);

        //DTO 만들기
        ProposalFormDataDto dataDto = new ProposalFormDataDto(
                savedProposalForm.getId(),
                savedProposalForm.getTitle(),
                savedProposalForm.getContent(),
                savedProposalForm.getRequestForm().getDesiredPrice(),
                savedProposalForm.getRequestForm().getPickupDate(),
                savedProposalForm.getCreatedAt(),
                savedProposalForm.getStatus().name()
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
                .orElseThrow(() -> new RuntimeException("해당 제안서가 존재하지 않습니다."));

        //RequestForm 객체 가져오기
        RequestForm requestForm = foundProposalForm.getRequestForm();

        //DTO 만들기(ProposalForm과 RequestForm 데이터를 합쳐 DTO 생성)

        //ProposalFormDto 만들기
        ProposalFormDataDto proposalFormDataDto = new ProposalFormDataDto(
                foundProposalForm.getId(),
                foundProposalForm.getTitle(),
                foundProposalForm.getContent(),
                requestForm.getDesiredPrice(),
                requestForm.getPickupDate(),
                foundProposalForm.getCreatedAt(),
                foundProposalForm.getStatus().name()
        );

        //RequestFormDto 만들기
        RequestFormDataDto requestFormDataDto = new RequestFormDataDto(
                requestForm.getId(),
                requestForm.getTitle(),
                requestForm.getDesiredPrice(),
                requestForm.getPickupDate(),
                requestForm.getStatus().name(),
                requestForm.getCreatedAt(),
                requestForm.getImage()
        );

        //응답 DTO 만들기
        ProposalFormContainsRequestFormDataDto responseDto = new ProposalFormContainsRequestFormDataDto(proposalFormDataDto, requestFormDataDto);

        //반환
        ApiResponse<ProposalFormContainsRequestFormDataDto> response = ApiResponse.success(HttpStatus.OK, "success", responseDto);
        return response;
    }
    /**
     * proposalForm 목록 조회
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
                            requestForm.getDesiredPrice(),
                            requestForm.getPickupDate(),
                            proposalForm.getCreatedAt(),
                            proposalForm.getStatus().name()
                    );

                    RequestFormDataDto requestDto = new RequestFormDataDto(
                            requestForm.getId(),
                            requestForm.getTitle(),
                            requestForm.getDesiredPrice(),
                            requestForm.getPickupDate(),
                            requestForm.getStatus().name(),
                            requestForm.getCreatedAt(),
                            requestForm.getImage()
                    );

                    return new ProposalFormContainsRequestFormDataDto(proposalDto, requestDto);
                })
                .collect(Collectors.toList());

        //응답 DTO 만들기
        ApiResponse<List<ProposalFormContainsRequestFormDataDto>> response = ApiResponse.success(
                HttpStatus.OK, "success", dataList);
        //반환
        return response;
    }
}
