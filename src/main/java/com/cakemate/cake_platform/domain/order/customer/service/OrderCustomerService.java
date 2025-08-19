package com.cakemate.cake_platform.domain.order.customer.service;

import com.cakemate.cake_platform.common.exception.*;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.exception.CustomerNotFoundException;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.notification.service.NotificationService;
import com.cakemate.cake_platform.domain.order.common.OrderNumberGenerator;
import com.cakemate.cake_platform.domain.order.customer.dto.*;
import com.cakemate.cake_platform.domain.order.customer.exception.ProposalAlreadyOrderedException;
import com.cakemate.cake_platform.domain.order.customer.exception.ProposalFormNotConfirmedException;
import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.order.enums.OrderStatus;
import com.cakemate.cake_platform.domain.order.customer.exception.UnauthorizedRequestFormAccessException;
import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import com.cakemate.cake_platform.domain.payment.entity.Payment;
import com.cakemate.cake_platform.domain.payment.enums.PaymentStatus;
import com.cakemate.cake_platform.domain.payment.repository.PaymentRepository;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import com.cakemate.cake_platform.domain.proposalForm.repository.ProposalFormRepository;
import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import com.cakemate.cake_platform.domain.requestForm.enums.RequestFormStatus;
import com.cakemate.cake_platform.common.dto.PageDto;
import com.cakemate.cake_platform.domain.store.entity.Store;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderCustomerService {
    private final OrderRepository orderRepository;
    private final ProposalFormRepository proposalFormRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;



    public OrderCustomerService(OrderRepository orderRepository, ProposalFormRepository proposalFormRepository, CustomerRepository customerRepository, PaymentRepository paymentRepository, NotificationService notificationService, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.proposalFormRepository = proposalFormRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.notificationService = notificationService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 소비자 -> 주문 생성 Service
     */
    @Transactional
    public CustomerOrderCreateResponseDto createOrderService(Long customerId, Long proposalFormId, CustomerOrderCreateRequestDto requestDto) {
        ProposalForm proposalForm = proposalFormRepository.findByIdAndIsDeletedFalse(proposalFormId)
                .orElseThrow(() -> new ProposalFormNotFoundException("견적서가 존재하지 않습니다."));

        boolean isAlreadyOrdered = orderRepository.existsByProposalForm(proposalForm);
        if (isAlreadyOrdered) {
            throw new ProposalAlreadyOrderedException("이미 주문이 생성된 견적서입니다.");
        }

        RequestForm requestForm = Objects.requireNonNull(proposalForm.getRequestForm(), "견적서에 연결된 의뢰서가 없습니다.");
        Long requestFormId = requestForm.getId();

        // 토큰에서 가져온 소비자 Id와 의뢰서 작성한 소비자 Id 비교
        if (!requestForm.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedRequestFormAccessException("본인의 의뢰서가 아닙니다.");
        }

        // 견적서 상태 = CONFIRMED 확인
        if (proposalForm.getStatus() != ProposalFormStatus.CONFIRMED) {
            throw new ProposalFormNotConfirmedException("CONFIRMED 상태의 견적서에서만 주문을 생성할 수 있습니다.");
        }

        // 해당 의뢰서와 견적서는 완료 처리
        requestForm.updateStatus(RequestFormStatus.SELECTED);

        // 소비자가 선택한 견적서 외 다른 견적서들은 CANCELLED로 상태 변경
        List<ProposalForm> proposalFormList = proposalFormRepository.findOtherProposalsByRequestFormIdExceptSelected(requestFormId, proposalFormId);
        for (ProposalForm p : proposalFormList) {
            p.updateStatus(ProposalFormStatus.CANCELLED);
        }

        //견적서 취소 알림 보내기(소비자->점주)
        String message = "견적서가 취소되었습니다.";
        for (ProposalForm p : proposalFormList) {
            notificationService.sendNotification(p.getOwner().getId(), message, "owner");
        }

        // ProposalForm의 store가 nullable = true -> null 체크
        Store store = Optional.ofNullable(proposalForm.getStore())
                .orElseThrow(() -> new StoreNotFoundException("견적서에 가게가 존재하지 않습니다."));

        Customer customer = customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new CustomerNotFoundException());

        // 주문 번호 생성
        String orderNumber = OrderNumberGenerator.generateOrderNumber();

        // 주문 생성
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .customer(customer)
                .store(store)
                .requestForm(requestForm)
                .proposalForm(proposalForm)
                .status(OrderStatus.MAKE_WAITING)
                .customerName(requestDto.getCustomerName())
                .customerPhoneNumber(customer.getPhoneNumber())
                .storeBusinessName(store.getBusinessName())
                .storeName(store.getName())
                .productName(store.getName() + " - 커스텀 케이크")
                .storePhoneNumber(store.getPhoneNumber())
                .storeAddress(store.getAddress())
                .agreedPrice(proposalForm.getProposedPrice())
                .agreedPickupDate(proposalForm.getProposedPickupDate())
                .finalCakeImage(proposalForm.getImage())
                .build();

        Order savedOrder = orderRepository.save(order);

        Payment payment = new Payment(savedOrder, savedOrder.getAgreedPrice(), "간편결제", PaymentStatus.AWAITING_PAYMENT);

        paymentRepository.save(payment);

        //주문 생성 알림 보내기(소비자->점주)
        String orderMessage = "주문이 생성되었습니다.";
        notificationService.sendNotification(store.getOwner().getId(), orderMessage, "owner");

        CustomerOrderCreateResponseDto responseDto = new CustomerOrderCreateResponseDto(
                savedOrder.getId(),
                savedOrder.getOrderNumber(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt(),
                savedOrder.getAgreedPrice(),
                savedOrder.getProductName(),
                savedOrder.getCustomer().getEmail(),
                savedOrder.getCustomerName(),
                savedOrder.getCustomerPhoneNumber()
        );

        return responseDto;
    }

    /**
     * 소비자 -> 주문 상세 조회 Service
     */
    public CustomerOrderDetailResponseDto getCustomerOrderDetailService(Long customerId, Long orderId) {
        Order order = orderRepository.findByCustomerIdAndId(customerId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문 내역이 존재하지 않습니다."));

        CustomerOrderDetailResponseDto responseDto = new CustomerOrderDetailResponseDto(
                order.getId(),
                order.getOrderNumber(),
                order.getCreatedAt(),
                order.getStatus().toString(),
                order.getCustomerName(),
                order.getStoreName(),
                order.getStoreBusinessName(),
                order.getStorePhoneNumber(),
                order.getStoreAddress(),
                order.getRequestForm().getId(),
                order.getProposalForm().getId(),
                Optional.ofNullable(order.getProductName()).orElse("상품 정보 없음"),
                order.getProposalForm().getCakeSize().toString(),
                order.getProposalForm().getQuantity(),
                order.getAgreedPrice(),
                order.getAgreedPickupDate(),
                Optional.ofNullable(order.getFinalCakeImage()).orElse("케이크 이미지 없음")
        );

        return responseDto;
    }

    /**
     * 소비자 -> 주문 목록 조회 Service
     *
     * @param customerId
     * @param pageable
     * @return
     */
    public CustomerOrderPageResponseDto<CustomerOrderSummaryResponseDto> getCustomerOrderPageService(Long customerId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByRequestFormCustomerId(customerId, pageable);

        if (orderPage.isEmpty()) {
            throw new OrderNotFoundException("주문 내역이 존재하지 않습니다.");
        }

        List<CustomerOrderSummaryResponseDto> responseDtoList = orderPage.stream()
                .map(order -> {
                    Long orderId = order.getId();
                    String orderNumber = order.getOrderNumber();
                    String status = order.getStatus().toString();
                    String storeName = order.getStoreName();
                    LocalDateTime agreedPickupDate = order.getAgreedPickupDate();
                    LocalDateTime orderCreatedAt = order.getCreatedAt();

                    return new CustomerOrderSummaryResponseDto(
                            orderId,
                            orderNumber,
                            orderCreatedAt,
                            status,
                            storeName,
                            agreedPickupDate
                    );
                })
                .collect(Collectors.toList());

        PageDto pageDto = new PageDto(
                orderPage.getNumber() + 1,
                orderPage.getSize(),
                orderPage.getTotalPages(),
                orderPage.getTotalElements()
        );

        CustomerOrderPageResponseDto<CustomerOrderSummaryResponseDto> responseDto = new CustomerOrderPageResponseDto<>(responseDtoList, pageDto);
        return responseDto;
    }
}
