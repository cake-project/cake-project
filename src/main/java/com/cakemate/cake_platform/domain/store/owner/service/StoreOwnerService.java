package com.cakemate.cake_platform.domain.store.owner.service;



import com.cakemate.cake_platform.domain.auth.entity.Owner;


import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.store.customer.command.StoreOwnerCommand;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.owner.dto.*;
import com.cakemate.cake_platform.domain.store.owner.exception.*;
import com.cakemate.cake_platform.domain.store.owner.repository.StoreOwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreOwnerService {
    private final StoreOwnerRepository storeOwnerRepository;
    private final OwnerRepository ownerRepository;

    public StoreOwnerService(
            StoreOwnerRepository storeOwnerRepository,
            OwnerRepository ownerRepository
    ) {
        this.storeOwnerRepository = storeOwnerRepository;
        this.ownerRepository = ownerRepository;
    }


    //가게 등록 Service
    @Transactional
    public StoreCreateResponseDto createStore(StoreOwnerCommand command, StoreCreateRequestDto requestDto) {

        // Long ownerId로 부터 Owner 엔티티 조회
        Owner owner = ownerRepository.findById(command.getOwnerId())
                .orElseThrow(() -> new NotFoundOwnerException("해당 점주가 존재하지 않습니다."));
        //필수값 검증
        boolean exist = storeOwnerRepository.existsByBusinessNumber(requestDto.getBusinessNumber());
        if (exist) {
            throw new DuplicateBusinessNumberException("이미 등록된 사업자번호입니다.");
        }
        // 2. 이미 가게가 존재하는지 확인
        boolean exists = storeOwnerRepository.existsByOwnerId(command.getOwnerId());
        if (exists) {
            throw new DuplicatedStoreException("이미 등록된 가게가 존재합니다.");
        }
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
    public StoreDetailResponseDto getStoreDetail(StoreOwnerCommand command) {

        Store store = storeOwnerRepository.findByOwnerId(command.getOwnerId())
                .orElseThrow(() -> new StoreNotFoundException("가게 정보가 존재하지 않습니다."));
        //권한 조회
        if (!store.getOwner().getId().equals(command.getOwnerId())) {
            throw new AccessDeniedException("본인의 가게만 조회할 수 있습니다.");
        }

        return new StoreDetailResponseDto(store);
    }
    //가게 수정 Service
    @Transactional
    public StoreUpdateResponseDto updateStore(StoreOwnerCommand command, StoreUpdateRequestDto requestDto) {


        boolean ownerExists = ownerRepository.existsById(command.getOwnerId());
        if (!ownerExists) {
            throw new NotFoundOwnerException("해당 점주가 존재하지 않습니다.");
        }


        Store store = storeOwnerRepository.findById(command.getStoreId())
                .orElseThrow(() -> new StoreNotFoundException("해당 가게가 존재하지 않습니다."));

        if (!store.getOwner().getId().equals(command.getOwnerId())) {
            throw new AccessDeniedException("본인의 가게만 수정할 수 있습니다.");
        }

        store.update(requestDto); // 엔티티 내부에서 값 수정
        return new StoreUpdateResponseDto(store);
    }
    //가게 삭제 Service
    @Transactional
    public void deleteStore(StoreOwnerCommand command) {

        // 1. Owner 조회
        boolean ownerExists = ownerRepository.existsById(command.getOwnerId());
        if (!ownerExists) {
            throw new NotFoundOwnerException("해당 점주가 존재하지 않습니다.");
        }


        // 2. Owner Store 조회
        Store store = storeOwnerRepository.findById(command.getStoreId())
                .orElseThrow(() -> new StoreNotFoundException("해당 가게가 존재하지 않습니다."));
        //3.권한 조회
        if (!store.getOwner().getId().equals(command.getOwnerId())) {
            throw new AccessDeniedException("본인의 가게만 삭제할 수 있습니다.");
        }

        //Soft delete 처리
        store.setIsDeleted(true);

        // 영속성 컨텍스트 반영 위해 저장 (Optional, 영속 상태라면 자동 반영)
        storeOwnerRepository.save(store);
    }
}
