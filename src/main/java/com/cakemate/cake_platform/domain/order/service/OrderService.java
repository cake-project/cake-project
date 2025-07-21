package com.cakemate.cake_platform.domain.order.service;

import com.cakemate.cake_platform.domain.order.dto.OrderCreateRequestDto;
import com.cakemate.cake_platform.domain.order.dto.OrderCreateResponseDto;
import com.cakemate.cake_platform.domain.order.dto.OrderDetailResponseDto;
import com.cakemate.cake_platform.domain.order.dto.OrderPageResponseDto;
import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.owner.dto.PageDto;
import com.cakemate.cake_platform.domain.requestForm.repository.RequestFormRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RequestFormRepository requestFormRepository;
    private final ProposalFormRepository proposalFormRepository;

    public OrderService(OrderRepository orderRepository, RequestFormRepository requestFormRepository, ProposalFormRepository proposalFormRepository) {
        this.orderRepository = orderRepository;
        this.requestFormRepository = requestFormRepository;
        this.proposalFormRepository = proposalFormRepository;
    }

    // 주문 생성 Service
    public OrderCreateResponseDto createOrderService(OrderCreateRequestDto orderCreateRequestDto) {
        RequestForm requestForm = requestFormRepository.findById(orderCreateRequestDto.getRequestFormId())
                .orElseThrow(()-> new IllegalArgumentException("의뢰서가 존재하지 않습니다."));

        ProposalForm proposalForm = proposalFormRepository.findById(orderCreateRequestDto.getProposalFormId())
                .orElseThrow(()-> new IllegalArgumentException("견적서가 존재하지 않습니다."));

        if (orderRepository.existsByRequestForm(requestForm)) {
            throw new IllegalArgumentException("이미 주문이 생성된 의뢰서입니다.");
        }

        Order order = new Order(requestForm, proposalForm, OrderStatus.MAKE_WAITING);

        orderRepository.save(order);

        OrderCreateResponseDto responseDto = new OrderCreateResponseDto(
                order.getId(), order.getStatus().toString(), order.getRequestForm().getCustomer().getName(), order.getProposalForm().getStore().getName(), LocalDateTime.now()
        );

        return responseDto;

    }

    // 소비자 -> 주문 목록 조회 Service
    @Transactional(readOnly = true)
    public OrderPageResponseDto getCustomerOrderPageService(Long customerId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByRequestFormCustomerId(customerId, pageable);

        if (orderPage.isEmpty()) {
            throw new IllegalArgumentException("주문 내역이 존재하지 않습니다.");
        }
        List<OrderDetailResponseDto> responseDtoList = orderPage.stream()
                .map(order -> new OrderDetailResponseDto(
                        order.getId(),
                        order.getStatus().toString(),
                        order.getRequestForm().getCustomer().getName(),
                        order.getProposalForm().getStore().getName(),
                        order.getRequestForm().getTitle(),
                        order.getRequestForm().getRegion(),
                        order.getRequestForm().getDesiredPrice()
                )).collect(Collectors.toList());

        PageDto pageDto = new PageDto(
                orderPage.getNumber() + 1,
                orderPage.getSize(),
                orderPage.getTotalPages(),
                orderPage.getTotalElements()
        );

        OrderPageResponseDto responseDto = new OrderPageResponseDto(responseDtoList, pageDto);

        return responseDto;
    }
}
