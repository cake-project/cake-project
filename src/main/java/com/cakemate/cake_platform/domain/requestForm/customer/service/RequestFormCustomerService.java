package com.cakemate.cake_platform.domain.requestForm.customer.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.exception.BadRequestException;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.request.CustomerRequestFormCreateRequestDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CustomerRequestFormCreateResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CustomerRequestFormGetDetailResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.CustomerRequestFormGetListResponseDto;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.cakemate.cake_platform.domain.requestForm.exception.RequestFormAccessDeniedException;
import com.cakemate.cake_platform.domain.requestForm.exception.NotFoundRequestFormException;
import com.cakemate.cake_platform.domain.store.owner.exception.DuplicateBusinessNumberException;
import com.cakemate.cake_platform.domain.store.owner.exception.DuplicatedStoreException;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundCustomerException;
import com.cakemate.cake_platform.domain.store.owner.exception.NotFoundOwnerException;
import org.springframework.http.HttpStatus;
import com.cakemate.cake_platform.domain.requestForm.repository.RequestFormRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional
    public ApiResponse<CustomerRequestFormCreateResponseDto> createRequestFormService(
            CustomerRequestFormCreateRequestDto requestFormCustomerRequestDto,  Long customerId
    ) {
        //데이터 준비
        String foundTitle = requestFormCustomerRequestDto.getTitle();
        String foundRegion = requestFormCustomerRequestDto.getRegion();
        String foundContent = requestFormCustomerRequestDto.getContent();
        int foundDesiredPrice = requestFormCustomerRequestDto.getDesiredPrice();
        String foundImage = requestFormCustomerRequestDto.getImage();
        LocalDateTime foundPickupDate = requestFormCustomerRequestDto.getPickupDate();
        RequestFormStatus requestFormStatus = RequestFormStatus.REQUESTED;

        //검증로직 작성 필요시
        // 검증.-> 본인 여부 검증
        if (customerId == null) {
            throw new BadRequestException("유효한 사용자 정보가 아닙니다.");
        }

        //고객(커스터머)아이디가 존재하면 통과, 아니면 예외 발생
        Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new NotFoundCustomerException("존재하지 않는 고객입니다."));

        //엔티티만들기
        RequestForm newRequestForm = new RequestForm(
                customer, foundTitle, foundRegion,
                foundContent, foundDesiredPrice, foundImage, foundPickupDate, requestFormStatus
        );

        //저장
        RequestForm saveRequestForm = requestFormRepository.save(newRequestForm);
        CustomerRequestFormCreateResponseDto requestFormCustomerResponseDto
                = new CustomerRequestFormCreateResponseDto(
                        saveRequestForm.getId(), saveRequestForm.getTitle(),
                saveRequestForm.getStatus(), saveRequestForm.getCreatedAt(), saveRequestForm.getDesiredPickupDate()
        );
        return ApiResponse.success(
                HttpStatus.OK, "의뢰가 성공적으로 등록되었습니다.", requestFormCustomerResponseDto
        );
    }

    /**
     * 고객 의뢰 단건조회 서비스
     */
    @Transactional
    public ApiResponse<CustomerRequestFormGetDetailResponseDto> getDetailRequestFormService(
            Long requestFormId, Long customerId
    ) {
        //삭제되지 않은 의뢰서 조회 & 검증
        RequestForm requestForm = requestFormRepository.findByIdAndIsDeletedFalse(requestFormId)
                .orElseThrow(() -> new NotFoundRequestFormException("조회할 의뢰서를 찾을 수 없습니다."));

        // 검증.-> 본인 여부 검증
        if (!requestForm.getCustomer().getId().equals(customerId)) {
            // 작성자가 아니면 예외 발생
            throw new RequestFormAccessDeniedException("본인이 작성한 의뢰서만 조회할 수 있습니다.");
        }

        //해당 의뢰서에 연결된 견적서 목록 조회 & 검증
        //proposalForm 은 없을 수도 있고, 있을 수도 있는 필드 ->널 허용: 없으면 빈 리스트
        List<ProposalForm> proposalFormList =
                proposalFormRepository.findAllByRequestFormIdAndIsDeletedFalse(requestFormId);

        //responseDto 만들기 (RequestFormGetDetailDto)
        CustomerRequestFormGetDetailResponseDto.RequestFormGetDetailDto requestFormGetDetailDto
                = new CustomerRequestFormGetDetailResponseDto.RequestFormGetDetailDto(
                requestForm.getId(),
                requestForm.getTitle(),
                requestForm.getRegion(),
                requestForm.getContent(),
                requestForm.getDesiredPrice(),
                requestForm.getImage(),
                requestForm.getDesiredPickupDate(),
                requestForm.getStatus(),
                requestForm.getCreatedAt()
        );


        //responseDto 만들기 (ProposalGetListInternalDto)
        List<CustomerRequestFormGetDetailResponseDto.ProposalGetListInternalDto> proposalGetListInternalDto
                = proposalFormList.stream()
                .map(
                        responseProposalForm ->
                                new CustomerRequestFormGetDetailResponseDto.ProposalGetListInternalDto(
                                        responseProposalForm.getId(),
                                        responseProposalForm.getStoreName(),
                                        responseProposalForm.getTitle(),
                                        responseProposalForm.getContent(),
                                        responseProposalForm.getProposedPrice(),
                                        responseProposalForm.getProposedPickupDate(),
                                        responseProposalForm.getImage(),
                                        responseProposalForm.getStatus(),
                                        responseProposalForm.getCreatedAt()
                                )
                ).collect(Collectors.toList());

        CustomerRequestFormGetDetailResponseDto DetailResponseDto
                = new CustomerRequestFormGetDetailResponseDto(requestFormGetDetailDto, proposalGetListInternalDto);


        //responseDto 반환
        return ApiResponse.success(
                HttpStatus.OK, "의뢰서를 성공적으로 조회했습니다.", DetailResponseDto
        );
    }

    /**
     * 고객 의뢰 다건조회 서비스
     */
    @Transactional
    public ApiResponse<List<CustomerRequestFormGetListResponseDto>> getListRequestFormService(
            Long requestFormId, Long customerId
            ) {
        //조회 & 검증
        RequestForm requestForm = requestFormRepository.findByIdAndIsDeletedFalse(requestFormId)
                .orElseThrow(() -> new NotFoundRequestFormException("조회할 의뢰서를 찾을 수 없습니다."));

        // 검증.-> 본인 여부 검증
        if (!requestForm.getCustomer().getId().equals(customerId)) {
            // 작성자가 아니면 예외 발생
            throw new RequestFormAccessDeniedException("본인이 작성한 의뢰서만 조회할 수 있습니다.");
        }

        List<RequestForm> allRequestForm = requestFormRepository.findAllByIsDeletedFalse();

        List<CustomerRequestFormGetListResponseDto> list = allRequestForm.stream()
                .map(newRequestForm -> new CustomerRequestFormGetListResponseDto(
                        newRequestForm.getId(), newRequestForm.getTitle(),
                        newRequestForm.getStatus(), newRequestForm.getCreatedAt()
                )).toList();
        return ApiResponse.success(
                HttpStatus.OK, "의뢰서 목록을 성공적으로 조회했습니다.", list
        );
    }
    /**
     * 고객 의뢰 삭제 서비스
     */
    @Transactional
    public ApiResponse<Object> deleteListRequestFormService(Long requestFormId, Long authenticatedCustomerId) {
        //조회 & 검증 -> 의뢰서 존재 여부
        RequestForm requestForm = requestFormRepository.findByIdAndIsDeletedFalse(requestFormId)
                .orElseThrow(() -> new NotFoundRequestFormException("삭제할 의뢰서를 찾을 수 없습니다."));

        // 검증.-> 본인 여부 검증
        if (!requestForm.getCustomer().getId().equals(authenticatedCustomerId)) {
            // 작성자가 아니면 예외 발생
            throw new RequestFormAccessDeniedException("본인이 작성한 의뢰서만 삭제할 수 있습니다.");
        }
        //삭제(소프트 딜리트)
        requestForm.softDelete();

        //반환
        return ApiResponse.success(
                HttpStatus.OK, "의뢰서가 성공적으로 삭제되었습니다.", null
        );

    }

}