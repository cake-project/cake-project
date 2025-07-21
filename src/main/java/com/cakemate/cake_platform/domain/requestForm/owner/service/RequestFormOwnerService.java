package com.cakemate.cake_platform.domain.requestForm.owner.service;

import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.cakemate.cake_platform.domain.requestForm.owner.dto.PageDto;
import com.cakemate.cake_platform.domain.requestForm.owner.dto.RequestFormDetailOwnerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.owner.dto.RequestFormPageOwnerResponseDto;
import com.cakemate.cake_platform.domain.requestForm.repository.RequestFormRepository;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestFormOwnerService {

    private final RequestFormRepository requestFormRepository;
    private final StoreRepository storeRepository;

    public RequestFormOwnerService(RequestFormRepository requestFormRepository, StoreRepository storeRepository) {
        this.requestFormRepository = requestFormRepository;
        this.storeRepository = storeRepository;
    }

    // 점주 -> 의뢰서 단건 조회 Service
    public RequestFormDetailOwnerResponseDto getRequestDetailOwnerService(Long ownerId, Long requestFormId) {

        // 데이터 준비
        RequestForm requestForm = requestFormRepository.findById(requestFormId)
                .orElseThrow(() -> new IllegalArgumentException("의뢰서가 존재하지 않습니다."));

        Long id = requestForm.getId();
        String name = requestForm.getCustomer().getName();
        String title = requestForm.getTitle();
        String region = requestForm.getRegion();
        String content = requestForm.getContent();
        int desiredPrice = requestForm.getDesiredPrice();
        String image = requestForm.getImage();
        LocalDateTime pickupDate = requestForm.getPickupDate();
        RequestFormStatus status = requestForm.getStatus();
        LocalDateTime createAt = requestForm.getCreatedAt();

        RequestFormDetailOwnerResponseDto responseDto = new RequestFormDetailOwnerResponseDto(id, name, title, region, content, desiredPrice, image, pickupDate, status, createAt);
        return responseDto;
    }

    // 점주 -> 의뢰서 목록 조회 Service
    public RequestFormPageOwnerResponseDto<RequestFormDetailOwnerResponseDto> getRequestListOwnerService(Long ownerId, Pageable pageable) {
        // 오너 아이디로 가게 찾기 -> 가게 아이디로 가게 주소 찾기 -> 가게 주소랑 의뢰서 지역 일치하는 의뢰서 찾기
        Store store = storeRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("가게가 존재하지 않습니다."));

        String storeAddress = store.getAddress();

        List<String> regions = List.of("서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종",
                "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주");

        String cityName = "unknown";
        for (String region : regions) {
            if (storeAddress.startsWith(region)) {
                cityName = region;
                break;
            }
        }

        if (cityName.equals("unknown")) {
            throw new IllegalArgumentException("가게 근처의 의뢰서를 찾을 수 없습니다.");
        }

        Page<RequestForm> requestFormPage = requestFormRepository.findByRegion(cityName, pageable);

        if (requestFormPage.isEmpty()) {
            throw new IllegalArgumentException("의뢰서가 존재하지 않습니다.");
        }

        PageDto pageDto = new PageDto(requestFormPage.getNumber() + 1, requestFormPage.getSize(), requestFormPage.getTotalPages(), requestFormPage.getTotalElements());
        List<RequestFormDetailOwnerResponseDto> responseDtoList = requestFormPage.getContent().stream()
                .map(requestForm -> new RequestFormDetailOwnerResponseDto(
                        requestForm.getId(),
                        requestForm.getCustomer().getName(),
                        requestForm.getTitle(),
                        requestForm.getRegion(),
                        requestForm.getContent(),
                        requestForm.getDesiredPrice(),
                        requestForm.getImage(),
                        requestForm.getPickupDate(),
                        requestForm.getStatus(),
                        requestForm.getCreatedAt()
                )).collect(Collectors.toList());
        RequestFormPageOwnerResponseDto<RequestFormDetailOwnerResponseDto> responseDto = new RequestFormPageOwnerResponseDto<>(responseDtoList, pageDto);
        return responseDto;
    }

}
