package com.customer.controller;

import com.customer.dto.CustomerRequest;
import com.customer.dto.CustomerResponse;
import com.customer.entity.Customer;
import com.customer.mapper.CustomerMapper;
import com.customer.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.OPTIONS})
@Tag(name = "Customer Microservice", description = "Endpoints for managing customer profiles and KYC status")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    // ✅ Add POST fallback for KYC Update in case PATCH is blocked
    @PostMapping("/{id}/kyc")
    public CustomerResponse updateKycStatusPost(
            @PathVariable Long id,
            @RequestParam String status) {
        return updateKycStatus(id, status);
    }

    private final CustomerService customerService;

    // ✅ Create Customer
    @PostMapping
    @Operation(summary = "Create a new customer", description = "Initializes a new customer profile with PENDING KYC status")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    public CustomerResponse createCustomer(@Valid @RequestBody CustomerRequest request) {
        logger.info("Entering createCustomer method");
        try {
            Customer customer = CustomerMapper.toEntity(request);
            Customer savedCustomer = customerService.createCustomer(customer);
            logger.info("Customer created successfully");
            return CustomerMapper.toResponse(savedCustomer);
        } catch (Exception e) {
            logger.error("Error in createCustomer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create customer: " + e.getMessage());
        }
    }

    // ✅ Get Customer by ID
    @GetMapping("/{id}")
    public CustomerResponse getCustomer(@PathVariable Long id) {
        logger.info("Entering getCustomer method with id: {}", id);
        try {
            Customer customer = customerService.getCustomerById(id);
            logger.info("Customer fetched successfully");
            return CustomerMapper.toResponse(customer);
        } catch (Exception e) {
            logger.error("Error in getCustomer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch customer: " + e.getMessage());
        }
    }

    // ✅ Get All Customers
    @GetMapping
    @Operation(summary = "Get all customers", description = "Returns a list of all registered customers")
    public List<CustomerResponse> getAllCustomers() {
        logger.info("Entering getAllCustomers method");
        try {
            return customerService.getAllCustomers()
                    .stream()
                    .map(CustomerMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in getAllCustomers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch all customers: " + e.getMessage());
        }
    }

    // ✅ Update Full Customer
    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        logger.info("Entering updateCustomer method with id: {}", id);
        try {
            Customer updatedCustomer = CustomerMapper.toEntity(request);
            Customer saved = customerService.updateCustomer(id, updatedCustomer);
            logger.info("Customer updated successfully");
            return CustomerMapper.toResponse(saved);
        } catch (Exception e) {
            logger.error("Error in updateCustomer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update customer: " + e.getMessage());
        }
    }

    // ✅ Update KYC Status
    @PatchMapping("/{id}/kyc")
    @Operation(summary = "Update KYC Status (PATCH)", description = "Updates the KYC status of a customer. Used for granular updates.")
    public CustomerResponse updateKycStatus(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Parameter(description = "New KYC Status (VERIFIED, REJECTED, PENDING)") @RequestParam String status) {
        logger.info("Entering updateKycStatus method with id: {} and status: {}", id, status);
        try {
            Customer updated = customerService.updateCustomerKycStatus(id, status);
            logger.info("KYC status updated successfully");
            return CustomerMapper.toResponse(updated);
        } catch (Exception e) {
            logger.error("Error in updateKycStatus: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update KYC status: " + e.getMessage());
        }
    }

    // ✅ Update Phone
    @PatchMapping("/{id}/phone")
    public CustomerResponse updatePhone(
            @PathVariable Long id,
            @RequestParam String phone) {
        logger.info("Entering updatePhone method with id: {} and phone: {}", id, phone);
        try {
            Customer updated = customerService.updateCustomerPhone(id, phone);
            logger.info("Phone updated successfully");
            return CustomerMapper.toResponse(updated);
        } catch (Exception e) {
            logger.error("Error in updatePhone: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update phone: " + e.getMessage());
        }
    }

    // ✅ Update Email
    @PatchMapping("/{id}/email")
    public CustomerResponse updateEmail(
            @PathVariable Long id,
            @RequestParam String email) {
        logger.info("Entering updateEmail method with id: {} and email: {}", id, email);
        try {
            Customer updated = customerService.updateCustomerEmail(id, email);
            logger.info("Email updated successfully");
            return CustomerMapper.toResponse(updated);
        } catch (Exception e) {
            logger.error("Error in updateEmail: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update email: " + e.getMessage());
        }
    }

    // ✅ Update Name
    @PatchMapping("/{id}/name")
    public CustomerResponse updateName(
            @PathVariable Long id,
            @RequestParam String name) {
        logger.info("Entering updateName method with id: {} and name: {}", id, name);
        try {
            Customer updated = customerService.updateCustomerName(id, name);
            logger.info("Name updated successfully");
            return CustomerMapper.toResponse(updated);
        } catch (Exception e) {
            logger.error("Error in updateName: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update name: " + e.getMessage());
        }
    }

    // ✅ Delete Customer
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer", description = "Permanently removes a customer record from the system")
    public void deleteCustomer(@PathVariable Long id) {
        logger.info("Entering deleteCustomer method with id: {}", id);
        try {
            customerService.deleteCustomer(id);
            logger.info("Customer deleted successfully");
        } catch (Exception e) {
            logger.error("Error in deleteCustomer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete customer: " + e.getMessage());
        }
    }

    // ✅ Upload Customers CSV
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @Operation(summary = "Bulk upload customers via CSV", description = "Accepts a CSV file to seed or update the customer database in bulk")
    public String uploadCustomers(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        logger.info("Entering uploadCustomers method with file: {}", file.getOriginalFilename());
        try {
            customerService.uploadCustomersFromCsv(file);
            logger.info("Customers uploaded successfully from CSV");
            return "Customers uploaded successfully from CSV.";
        } catch (Exception e) {
            logger.error("Error in uploadCustomers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload customers: " + e.getMessage());
        }
    }

    // ✅ Check if Customer Exists (Synchronous check for other services)
    @GetMapping("/{id}/exists")
    @Operation(summary = "Check if customer exists", description = "Lightweight endpoint for other microservices to validate a customer ID")
    public boolean checkCustomerExists(@PathVariable Long id) {
        logger.info("Entering checkCustomerExists method with id: {}", id);
        try {
            // Re-using getCustomerById which throws exception if not found
            // For a pure boolean check we could just check repo directly, but this is simple.
            customerService.getCustomerById(id);
            return true;
        } catch (com.customer.exception.ResourceNotFoundException e) {
            return false;
        } catch (Exception e) {
            logger.error("Error in checkCustomerExists: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to check customer existence: " + e.getMessage());
        }
    }
}