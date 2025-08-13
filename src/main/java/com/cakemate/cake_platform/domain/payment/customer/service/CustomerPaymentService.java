package com.cakemate.cake_platform.domain.payment.customer.service;

import com.cakemate.cake_platform.common.exception.OrderNotFoundException;
import com.cakemate.cake_platform.common.exception.UnauthorizedAccessException;
import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.order.repository.OrderRepository;
import com.cakemate.cake_platform.domain.payment.customer.dto.CustomerPaymentConfirmRequestDto;
import com.cakemate.cake_platform.domain.payment.customer.dto.CustomerPaymentConfirmResponseDto;
import com.cakemate.cake_platform.domain.payment.customer.exception.PaymentFailedException;
import com.cakemate.cake_platform.domain.payment.customer.exception.PaymentNotFoundException;
import com.cakemate.cake_platform.domain.payment.entity.Payment;
import com.cakemate.cake_platform.domain.payment.enums.PaymentStatus;
import com.cakemate.cake_platform.domain.payment.repository.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Base64;

@Service
public class CustomerPaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public CustomerPaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * 소비자 -> 결제 승인 Service
     *
     * @param customerId
     * @param requestDto
     */
    @Transactional
    public CustomerPaymentConfirmResponseDto confirmCustomerPayment(Long customerId, CustomerPaymentConfirmRequestDto requestDto) {
        String orderNumber = requestDto.getOrderId();
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedAccessException("본인의 주문이 아닙니다.");
        }

        try {
            String jsonBody = String.format(
                    "{\"paymentKey\":\"%s\",\"orderId\":\"%s\",\"amount\":%d}",
                    requestDto.getPaymentKey(),
                    requestDto.getOrderId(),
                    requestDto.getAmount()
            );

            String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
            String authorizations = "Basic " + new String(encodedBytes);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                    .header("Authorization", authorizations)
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

            // response JSON 값 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            String tossStatus = jsonNode.get("status").asText();
            if (!tossStatus.equals("DONE")) {
                throw new PaymentFailedException("결제 승인에 실패했습니다. 상태값 = " + tossStatus);
            }

            String paymentKey = jsonNode.get("paymentKey").asText();
            int totalAmount = jsonNode.get("totalAmount").asInt();
            String method = jsonNode.get("method").asText();
            String easyPayProvider = jsonNode.get("easyPay").get("provider").asText();
            String receiptUrl = jsonNode.get("receipt").get("url").asText();
            LocalDateTime requestedAt = OffsetDateTime.parse(jsonNode.get("requestedAt").asText()).toLocalDateTime();
            LocalDateTime approvedAt = OffsetDateTime.parse(jsonNode.get("approvedAt").asText()).toLocalDateTime();

            Payment payment = paymentRepository.findByOrder(order)
                    .orElseThrow(() -> new PaymentNotFoundException("결제 정보가 존재하지 않습니다."));

            Payment updatePayment = payment.updatePayment(PaymentStatus.COMPLETED, paymentKey, totalAmount, method, easyPayProvider, receiptUrl, requestedAt, approvedAt);

            CustomerPaymentConfirmResponseDto responseDto = new CustomerPaymentConfirmResponseDto(
                    updatePayment.getPaymentKey(),
                    updatePayment.getAmount(),
                    updatePayment.getMethod(),
                    updatePayment.getReceiptUrl(),
                    updatePayment.getApprovedAt()
            );

            return responseDto;

        } catch (IOException | InterruptedException e) {
            throw new PaymentFailedException("결제 요청 중 오류가 발생했습니다.");
        }
    }
}
