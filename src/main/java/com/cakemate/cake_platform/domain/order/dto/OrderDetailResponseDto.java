package com.cakemate.cake_platform.domain.order.dto;

public class OrderDetailResponseDto {

    private Long orderId;
    private String orderStatus;
    private String customerName;
    private String storeName;
    private String requestTitle;
    private String requestRegion;
    private int desiredPrice;

    public OrderDetailResponseDto(Long orderId, String orderStatus, String customerName, String storeName, String requestTitle, String requestRegion, int desiredPrice) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.customerName = customerName;
        this.storeName = storeName;
        this.requestTitle = requestTitle;
        this.requestRegion = requestRegion;
        this.desiredPrice = desiredPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getRequestTitle() {
        return requestTitle;
    }

    public String getRequestRegion() {
        return requestRegion;
    }

    public int getDesiredPrice() {
        return desiredPrice;
    }
}
