package com.cakemate.cake_platform.domain.store.owner.service;

import com.cakemate.cake_platform.domain.auth.owner.entity.Owner;
import com.cakemate.cake_platform.domain.auth.owner.repository.OwnerRespository;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.owner.dto.StoreCreateRequestDto;
import com.cakemate.cake_platform.domain.store.owner.dto.StoreCreateResponseDto;
import com.cakemate.cake_platform.domain.store.owner.dto.StoreDetailResponseDto;
import com.cakemate.cake_platform.domain.store.owner.repository.StoreOwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class StoreOwnerService {
    private final StoreOwnerRepository storeOwnerRepository;
    private final OwnerRespository ownerRespository;

    public StoreOwnerService(StoreOwnerRepository storeOwnerRepository, OwnerRespository ownerRespository) {
        this.storeOwnerRepository = storeOwnerRepository;
        this.ownerRespository = ownerRespository;
    }

    public StoreCreateResponseDto createStore(StoreCreateRequestDto requestDto) {
        // Long ownerId로 부터 Owner 엔티티 조회
        Owner owner = ownerRespository.findById(requestDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사장님이 존재하지 않습니다."));
        //store 객체 생성
        Store store = new Store(
                owner,
                requestDto.getName(),
                requestDto.getAddress(),
                requestDto.getBusinessNumber(),
                requestDto.getPhoneNumber(),
                requestDto.getImage(),
                requestDto.isActive()
        );
        //store 저장
        Store savedStore = storeOwnerRepository.save(store);

        // 4. 응답 DTO 생성해서 반환
        return new StoreCreateResponseDto(savedStore);
    }

    public StoreDetailResponseDto getStoreDetail(Long ownerId) {
        Store store = storeOwnerRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        return new StoreDetailResponseDto(store);
    }

}
