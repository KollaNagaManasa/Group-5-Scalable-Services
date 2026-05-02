package com.customer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponse {

    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private String kycStatus;
    private java.time.LocalDateTime createdAt;
}