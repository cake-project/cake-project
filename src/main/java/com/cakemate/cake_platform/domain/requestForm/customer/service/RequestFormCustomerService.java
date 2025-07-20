package com.cakemate.cake_platform.domain.requestForm.customer.service;

import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.customer.entity.Customer;
import com.cakemate.cake_platform.domain.auth.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.request.RequestFormCustomerRequestDto;
import com.cakemate.cake_platform.domain.requestForm.customer.dto.response.RequestFormCustomerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.customer.repository.RequestFormCustomerRepository;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RequestFormCustomerService {

    //속
    private final RequestFormCustomerRepository requestFormCustomerRepository;
    private final ProposalFormRepository proposalFormRepository;
    private final CustomerRepository customerRepository;

    //생
    public RequestFormCustomerService(
            RequestFormCustomerRepository requestFormCustomerRepository, ProposalFormRepository proposalFormRepository, CustomerRepository customerRepository
    ) {
        this.requestFormCustomerRepository = requestFormCustomerRepository;
        this.proposalFormRepository = proposalFormRepository;
        this.customerRepository = customerRepository;
    }

    //기

    /**
     * 고객 의뢰 생성(등록) 서비스
     */
    //  "title": "돌잔치 케이크를 의뢰합니다.",
    //  "region": "서울시 마포구",
    //  "content": "2단 분홍색 케이크에 아기 그림을 원해요.",
    //  "desiredPrice": 30000,
    //  "image": "https://xxxxx.com/xxxx.png",
    //  "pickupDate": "yyyy-MM-dd hh-mm"
    public ApiResponse<RequestFormCustomerResponseDto> createRequestFormService(
            RequestFormCustomerRequestDto requestFormCustomerRequestDto
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
        RequestForm saveRequestForm = requestFormCustomerRepository.save(newRequestForm);
        RequestFormCustomerResponseDto requestFormCustomerResponseDto
                = new RequestFormCustomerResponseDto(saveRequestForm);
        return ApiResponse.success(
                HttpStatus.OK, "의뢰가 성공적으로 등록되었습니다.", requestFormCustomerResponseDto
        );
    }
}
