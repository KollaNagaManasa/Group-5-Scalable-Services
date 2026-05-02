package com.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEvent implements Serializable {
    private String eventId;
    private String eventType;
    private Long customerId;
    private String email;
    private String name;
    private String kycStatus;
    private LocalDateTime timestamp;
}
