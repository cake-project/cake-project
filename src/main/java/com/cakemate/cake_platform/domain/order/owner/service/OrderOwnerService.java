package com.cakemate.cake_platform.domain.order.owner.service;

import com.cakemate.cake_platform.common.dto.PageDto;
import com.cakemate.cake_platform.common.exception.OrderNotFoundException;
import com.cakemate.cake_platform.common.exception.StoreNotFoundException;
import com.cakemate.cake_platform.common.exception.UnauthorizedAccessException;
import com.cakemate.cake_platform.domain.notification.service.NotificationService;
import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.cakemate.cake_platform.domain.order.owner.dto.*;
import com.cakemate.cake_platform.domain.order.owner.exception.InvalidOrderStatusException;
import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.store.entity.Store;
import com.cakemate.cake_platform.domain.store.repository.StoreRepository;
import jakarta.validation.Valid;
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
    private final NotificationService notificationService;

    public OrderOwnerService(OrderRepository orderRepository, StoreRepository storeRepository, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.notificationService = notificationService;
    }

    /**
     * 점주(가게) -> 주문 상세 조회 Service
     */
    public OwnerOrderDetailResponseDto getOwnerStoreOrderDetail(Long storeId, Long ownerId, Long orderId) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedAccessException("본인 가게가 아닙니다.");
        }

        Order order = orderRepository.findByStoreIdAndId(storeId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        OwnerOrderDetailResponseDto responseDto = new OwnerOrderDetailResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getCreatedAt(),
                order.getStatus().toString(),
                order.getCustomerName(),
                order.getCustomerPhoneNumber(),
                order.getRequestForm().getId(),
                order.getProposalForm().getId(),
                Optional.ofNullable(order.getProductName()).orElse("상품 정보 없음"),
                order.getProposalForm().getCakeSize().toString(),
                order.getProposalForm().getQuantity(),
                order.getAgreedPrice(),
                order.getAgreedPickupDate(),
                order.getFinalCakeImage()
        );

        return responseDto;
    }

    /**
     * 점주(가게) -> 주문 목록 조회 Service
     */
    public OwnerOrderPageResponseDto<OwnerOrderSummaryResponseDto> getOwnerStoreOrderPage(Long storeId, Long ownerId, Pageable pageable) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedAccessException("본인 가게가 아닙니다.");
        }

        Page<Order> orderPage = orderRepository.findByStoreId(storeId, pageable);
        List<OwnerOrderSummaryResponseDto> orderList = orderPage.getContent().stream()
                .map(order -> new OwnerOrderSummaryResponseDto(
                        order.getId(),
                        order.getOrderNumber(),
                        order.getCreatedAt(),
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

    /**
     * 점주(가게) -> 주문 상태 수정 Service
     *
     * @param storeId
     * @param ownerId
     * @param requestDto
     */
    @Transactional
    public OwnerOrderStatusUpdateResponseDto updateStoreOrderStatusByOwner(Long storeId, Long orderId, Long ownerId, @Valid OwnerOrderStatusUpdateRequestDto requestDto) {
        Store store = storeRepository.findByIdAndIsDeletedFalse(storeId)
                .orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        if (!store.getOwner().getId().equals(ownerId)) {
            throw new UnauthorizedAccessException("본인 가게가 아닙니다.");
        }

        Order order = orderRepository.findByStoreIdAndId(storeId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        OrderStatus orderStatus;
        try {
            orderStatus = requestDto.getOrderStatusEnum();
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderStatusException("유효하지 않은 주문 상태 값입니다. 유효한 값: MAKE_WAITING, READY_FOR_PICKUP, COMPLETED, CANCELLED");
        }

        order.updateOrderStatus(orderStatus);

        // 주문 취소로 변경 시 견적서도 컨택 실패로 변경
        if (orderStatus == OrderStatus.CANCELLED) {
            Optional.ofNullable(order.getProposalForm())
                    .ifPresent(proposalForm -> proposalForm.updateStatus(ProposalFormStatus.CANCELLED));
        }

        //주문 상태 알림 보내기(점주->소비자)
        if (orderStatus == OrderStatus.READY_FOR_PICKUP) {
            String message = "케이크 제작이 완료되었습니다.";
            notificationService.sendNotification(order.getCustomer().getId(), message, "customer");
        } else if (orderStatus == OrderStatus.CANCELLED) {
            String message = "주문이 취소되었습니다.";
            notificationService.sendNotification(order.getCustomer().getId(), message, "customer");
        }

        OwnerOrderStatusUpdateResponseDto responseDto = new OwnerOrderStatusUpdateResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getModifiedAt(),
                order.getStoreName(),
                order.getCustomerName(),
                order.getAgreedPickupDate()
        );

        return responseDto;
    }
}
