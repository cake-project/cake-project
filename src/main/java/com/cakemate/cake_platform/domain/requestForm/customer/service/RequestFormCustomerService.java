package com.cakemate.cake_platform.domain.requestForm.customer.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.request.CreateRequestFormCustomerRequestDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CreateRequestFormCustomerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.GetDetailRequestFormCustomerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.GetListRequestFormCustomerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import org.springframework.http.HttpStatus;
import com.cakemate.cake_platform.domain.requestForm.repository.RequestFormRepository;
import com.cakemate.cake_platform.domain.requestForm.customer.repository.RequestFormRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestFormCustomerService {

    //속
    private final ProposalFormRepository proposalFormRepository;
    private final CustomerRepository customerRepository;
    private final RequestFormRepository requestFormRepository;

    //생
    public RequestFormCustomerService(
            RequestFormRepository requestFormRepository, ProposalFormRepository proposalFormRepository, CustomerRepository customerRepository
    ) {
        this.requestFormRepository = requestFormRepository;
        this.proposalFormRepository = proposalFormRepository;
        this.customerRepository = customerRepository;
    }

    //기

    /**
     * 고객 의뢰 생성(등록) 서비스
     */
    public ApiResponse<CreateRequestFormCustomerResponseDto> createRequestFormService(
            CreateRequestFormCustomerRequestDto requestFormCustomerRequestDto
    ) {
        //데이터 준비
        String foundTitle = requestFormCustomerRequestDto.getTitle();
        String foundRegion = requestFormCustomerRequestDto.getRegion();
        String foundContent = requestFormCustomerRequestDto.getContent();
        int foundDesiredPrice = requestFormCustomerRequestDto.getDesiredPrice();
        String foundImage = requestFormCustomerRequestDto.getImage();
        LocalDateTime foundPickupDate = requestFormCustomerRequestDto.getPickupDate();
        RequestFormStatus requestFormStatus = RequestFormStatus.REQUESTED;
        LocalDateTime foundCreatedAt = requestFormCustomerRequestDto.getCreatedAt();

        //검증로직 작성 필요시

        //proposalForm 은 없을 수도 있고, 있을 수도 있는 필드
        ProposalForm proposalForm = null;

        //찾으려는 견적서 아이디가 존재하면 통과, 아니면 예외 발생
        if (requestFormCustomerRequestDto.getProposalForm() != null &&
                requestFormCustomerRequestDto.getProposalForm().getId() != null) {
            proposalFormRepository.findById(
                    requestFormCustomerRequestDto.getProposalForm().getId()
            ).orElseThrow(() -> new RuntimeException("찾으려는 견적서는 존재하지 않습니다."));
        }
        //고객(커스터머)아이디가 존재하면 통과, 아니면 예외 발생
        Customer customer = customerRepository.findById(
                requestFormCustomerRequestDto.getCustomer().getId()
        ).orElseThrow(() -> new RuntimeException("존재하지 않는 고객입니다."));

        //엔티티만들기
        RequestForm newRequestForm = new RequestForm(
                proposalForm, customer, foundTitle, foundRegion,
                foundContent, foundDesiredPrice, foundImage, foundPickupDate, requestFormStatus
        );

        //저장
        RequestForm saveRequestForm = requestFormRepository.save(newRequestForm);
        CreateRequestFormCustomerResponseDto requestFormCustomerResponseDto
                = new CreateRequestFormCustomerResponseDto(
                        saveRequestForm.getId(), saveRequestForm.getTitle(),
                saveRequestForm.getStatus(), saveRequestForm.getCreatedAt()
        );
        return ApiResponse.success(
                HttpStatus.OK, "의뢰가 성공적으로 등록되었습니다.", requestFormCustomerResponseDto
        );
    }

    /**
     * 고객 의뢰 단건조회 서비스
     */
    public ApiResponse<GetDetailRequestFormCustomerResponseDto> getDetailRequestFormService(Long requestFormId) {
        //조회 & 검증
        RequestForm requestForm = requestFormRepository.findByIdAndIsDeletedFalse(
                requestFormId
        ).orElseThrow(() -> new RuntimeException("존재하지 않는 의뢰서 입니다."));
        Long foundRequestFormId = requestForm.getId();
        String foundRequestFormTitle = requestForm.getTitle();
        String foundRequestFormRegion = requestForm.getRegion();
        String foundRequestFormContent = requestForm.getContent();
        Integer foundRequestFormDesiredPrice = requestForm.getDesiredPrice();
        String foundRequestFormImage = requestForm.getImage();
        LocalDateTime foundRequestFormPickupDate = requestForm.getPickupDate();
        RequestFormStatus foundRequestFormStatus = requestForm.getStatus();
        LocalDateTime foundRequestFormCreatedAt = requestForm.getCreatedAt();

        //responseDto 만들기
        GetDetailRequestFormCustomerResponseDto responseDto = new GetDetailRequestFormCustomerResponseDto(
                foundRequestFormId,
                foundRequestFormTitle,
                foundRequestFormRegion,
                foundRequestFormContent,
                foundRequestFormDesiredPrice,
                foundRequestFormImage,
                foundRequestFormPickupDate,
                foundRequestFormStatus,
                foundRequestFormCreatedAt
        );
        //responseDto 반환
        return ApiResponse.success(
                HttpStatus.OK, "의뢰서를 성공적으로 조회했습니다.", responseDto
        );
    }

    /**
     * 고객 의뢰 다건조회 서비스
     */
    public ApiResponse<List<GetListRequestFormCustomerResponseDto>> getListRequestFormService() {
        //조회 & 검증
        List<RequestForm> allRequestForm = requestFormRepository.findAllByIsDeletedFalse();
        List<GetListRequestFormCustomerResponseDto> list = allRequestForm.stream()
                .map(requestForm -> new GetListRequestFormCustomerResponseDto(
                        requestForm.getId(), requestForm.getTitle(),
                        requestForm.getStatus(), requestForm.getCreatedAt()
                )).toList();
        return ApiResponse.success(
                HttpStatus.OK, "의뢰서 목록을 성공적으로 조회했습니다.", list
        );
    }
}