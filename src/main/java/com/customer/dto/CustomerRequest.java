package com.customer.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class CustomerRequest {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRequest.class);

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    public void setName(String name) {
        logger.debug("Setting name to: {}", name);
        this.name = name;
    }

    public void setEmail(String email) {
        logger.debug("Setting email to: {}", email);
        this.email = email;
    }

    public void setPhone(String phone) {
        logger.debug("Setting phone to: {}", phone);
        this.phone = phone;
    }
}