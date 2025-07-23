package com.cakemate.cake_platform.domain.store.owner.service;


import com.cakemate.cake_platform.common.jwt.utll.JwtUtil;
import com.cakemate.cake_platform.domain.auth.entity.Owner;


import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.owner.dto.*;
import com.cakemate.cake_platform.domain.store.owner.exception.DuplicatedStoreException;
import com.cakemate.cake_platform.domain.store.owner.exception.OwnerNotFoundException;
import com.cakemate.cake_platform.domain.store.owner.repository.StoreOwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreOwnerService {
    private final StoreOwnerRepository storeOwnerRepository;
    private final OwnerRepository ownerRepository;

    public StoreOwnerService(
            StoreOwnerRepository storeOwnerRepository,
            OwnerRepository ownerRepository,
            JwtUtil jwtUtil,
            MemberRepository memberRepository) {
        this.storeOwnerRepository = storeOwnerRepository;
        this.ownerRepository = ownerRepository;
    }


    //가게 등록 Service
    @Transactional
    public StoreCreateResponseDto createStore(StoreCreateRequestDto requestDto) {
        // Long ownerId로 부터 Owner 엔티티 조회
        Owner owner = ownerRepository.findById(requestDto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사장님이 존재하지 않습니다."));
        //store 객체 생성
        Store store = new Store(
                owner,
                requestDto.getBusinessName(),
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
    //가게 상세 조회 Service
    @Transactional(readOnly = true)
    public StoreDetailResponseDto getStoreDetail(Long ownerId) {
        Store store = storeOwnerRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));

        return new StoreDetailResponseDto(store);
    }
    //가게 수정 Service
    @Transactional
    public StoreUpdateResponseDto updateStore(Long ownerId, StoreUpdateRequestDto requestDto) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException("해당 점주가 존재하지 않습니다."));

        Store store = storeOwnerRepository.findByOwner(owner)
                .orElseThrow(() -> new RuntimeException("해당 오너의 가게가 존재하지 않습니다."));

        store.update(requestDto); // 엔티티 내부에서 값 수정
        return new StoreUpdateResponseDto(store);
    }
    //가게 삭제 Service
    @Transactional
    public void deleteStore(Long ownerId) {
        // 1. Owner 조회
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException("해당 점주가 존재하지 않습니다."));

        // 2. Owner의 Store 조회
        Store store = storeOwnerRepository.findByOwner(owner)
                .orElseThrow(() -> new RuntimeException("해당 오너의 가게가 존재하지 않습니다."));

        //Soft delete 처리
        store.setIsDeleted(true);

        // 영속성 컨텍스트 반영 위해 저장 (Optional, 영속 상태라면 자동 반영)
        storeOwnerRepository.save(store);
    }
}
