package com.cakemate.cake_platform.domain.order.owner.service;

import com.cakemate.cake_platform.common.dto.PageDto;
import com.cakemate.cake_platform.common.exception.OrderNotFoundException;
import com.cakemate.cake_platform.common.exception.StoreNotFoundException;
import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.order.owner.dto.OwnerOrderDetailResponseDto;
import com.cakemate.cake_platform.domain.order.owner.dto.OwnerOrderPageResponseDto;
import com.cakemate.cake_platform.domain.order.owner.dto.OwnerOrderSummaryResponseDto;
import com.cakemate.cake_platform.domain.order.owner.exception.UnauthorizedAccessException;
import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderOwnerService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public OrderOwnerService(OrderRepository orderRepository, StoreRepository storeRepository) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
    }

    /**
     * 점주 -> 주문 상세 조회 Service
     */
    public OwnerOrderDetailResponseDto getOwnerOrderDetailService(Long storeId, Long ownerId, Long orderId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedAccessException("본인 가게가 아닙니다.");
        }

        Order order = orderRepository.findByStoreIdAndId(storeId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        OwnerOrderDetailResponseDto responseDto = new OwnerOrderDetailResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getOrderCreatedAt(),
                order.getStatus().toString(),
                order.getCustomerName(),
                order.getCustomerPhoneNumber(),
                order.getRequestForm().getId(),
                order.getProposalForm().getId(),
                Optional.ofNullable(order.getProductName()).orElse("상품 정보 없음"),
                order.getAgreedPrice(),
                order.getAgreedPickupDate(),
                order.getFinalCakeImage()
        );

        return responseDto;
    }

    /**
     * 점주 -> 주문 목록 조회 Service
     */
    public OwnerOrderPageResponseDto<OwnerOrderSummaryResponseDto> getOwnerStoreOrderPageService(Long storeId, Long ownerId, Pageable pageable) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedAccessException("본인 가게가 아닙니다.");
        }

        Page<Order> orderPage = orderRepository.findByStoreId(storeId, pageable);
        List<OwnerOrderSummaryResponseDto> orderList = orderPage.getContent().stream()
                .map(order -> new OwnerOrderSummaryResponseDto(
                        order.getId(),
                        order.getOrderNumber(),
                        order.getOrderCreatedAt(),
                        order.getStatus().toString(),
                        order.getCustomerName(),
                        Optional.ofNullable(order.getProductName()).orElse("상품 정보 없음"),
                        order.getAgreedPrice(),
                        order.getAgreedPickupDate()
                ))
                .collect(Collectors.toList());

        PageDto pageDto = new PageDto(
                orderPage.getNumber() + 1,
                orderPage.getSize(),
                orderPage.getTotalPages(),
                orderPage.getTotalElements()
        );

        OwnerOrderPageResponseDto<OwnerOrderSummaryResponseDto> responseDto = new OwnerOrderPageResponseDto<>(orderList, pageDto);
        return responseDto;
    }
}
