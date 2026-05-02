package com.customer.mapper;

import com.customer.dto.CustomerRequest;
import com.customer.dto.CustomerResponse;
import com.customer.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerMapper {

    private static final Logger logger = LoggerFactory.getLogger(CustomerMapper.class);

    // DTO → Entity
    public static Customer toEntity(CustomerRequest request) {
        logger.info("Mapping CustomerRequest to Customer entity");
        try {
            return Customer.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .build();
        } catch (Exception e) {
            logger.error("Error in toEntity: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Entity → DTO
    public static CustomerResponse toResponse(Customer customer) {
        logger.info("Mapping Customer entity to CustomerResponse");
        try {
            return CustomerResponse.builder()
                    .customerId(customer.getId())
                    .name(customer.getName())
                    .email(customer.getEmail())
                    .phone(customer.getPhone())
                    .kycStatus(customer.getKycStatus())
                    .createdAt(customer.getCreatedAt())
                    .build();
        } catch (Exception e) {
            logger.error("Error in toResponse: {}", e.getMessage(), e);
            throw e;
        }
    }
}