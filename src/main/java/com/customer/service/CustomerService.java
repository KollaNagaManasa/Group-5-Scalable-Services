package com.customer.service;

import com.customer.entity.Customer;
import com.customer.exception.ResourceNotFoundException;
import com.customer.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerEventProducer eventProducer;


    public Customer getCustomerById(Long customerId) {
        logger.info("Entering getCustomerById with customerId: {}", customerId);
        try {
            return customerRepository.findById(customerId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Customer not found with id: " + customerId));
        } catch (Exception e) {
            logger.error("Error in getCustomerById: {}", e.getMessage(), e);
            throw e;
        }
    }


    public List<Customer> getAllCustomers() {
        logger.info("Entering getAllCustomers");
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            logger.error("Error in getAllCustomers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch all customers: " + e.getMessage());
        }
    }


    public Customer createCustomer(Customer customer) {
        logger.info("Entering createCustomer with customer: {}", customer);
        try {
            // Set default values
            if (customer.getCreatedAt() == null) {
                customer.setCreatedAt(LocalDateTime.now());
            }

            if (customer.getKycStatus() == null) {
                customer.setKycStatus("PENDING");
            }

            if (customer.getId() != null && customerRepository.existsById(customer.getId())) {
                throw new RuntimeException("Customer with ID " + customer.getId() + " already exists.");
            }

            Customer savedCustomer = customerRepository.save(customer);
            logger.info("Customer created with id: {}", savedCustomer.getId());

            // Publish Event
            com.customer.dto.CustomerEvent event = com.customer.dto.CustomerEvent.builder()
                    .eventId(java.util.UUID.randomUUID().toString())
                    .eventType("CUSTOMER_CREATED")
                    .customerId(savedCustomer.getId())
                    .name(savedCustomer.getName())
                    .email(savedCustomer.getEmail())
                    .kycStatus(savedCustomer.getKycStatus())
                    .timestamp(LocalDateTime.now())
                    .build();
            eventProducer.publishEvent(event);

            return savedCustomer;
        } catch (Exception e) {
            logger.error("Error in createCustomer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create customer: " + e.getMessage());
        }
    }


    public Customer updateCustomer(Long customerId, Customer updatedCustomer) {
        logger.info("Entering updateCustomer with customerId: {} and updatedCustomer: {}", customerId, updatedCustomer);
        try {
            Customer existing = getCustomerById(customerId);

            existing.setName(updatedCustomer.getName());
            existing.setEmail(updatedCustomer.getEmail());
            existing.setPhone(updatedCustomer.getPhone());

            Customer savedCustomer = customerRepository.save(existing);
            logger.info("Customer updated with id: {}", savedCustomer.getId());
            return savedCustomer;
        } catch (Exception e) {
            logger.error("Error in updateCustomer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update customer: " + e.getMessage());
        }
    }


    public Customer updateCustomerKycStatus(Long customerId, String kycStatus) {
        logger.info("Entering updateCustomerKycStatus with customerId: {} and kycStatus: {}", customerId, kycStatus);
        try {
            Customer customer = getCustomerById(customerId);

            if (kycStatus == null || kycStatus.isBlank()) {
                throw new RuntimeException("KYC status cannot be empty");
            }

            customer.setKycStatus(kycStatus.toUpperCase());

            Customer savedCustomer = customerRepository.save(customer);
            logger.info("Customer KYC status updated for id: {}", savedCustomer.getId());

            // Publish Event
            com.customer.dto.CustomerEvent event = com.customer.dto.CustomerEvent.builder()
                    .eventId(java.util.UUID.randomUUID().toString())
                    .eventType("KYC_STATUS_UPDATED")
                    .customerId(savedCustomer.getId())
                    .name(savedCustomer.getName())
                    .email(savedCustomer.getEmail())
                    .kycStatus(savedCustomer.getKycStatus())
                    .timestamp(LocalDateTime.now())
                    .build();
            eventProducer.publishEvent(event);

            return savedCustomer;
        } catch (Exception e) {
            logger.error("Error in updateCustomerKycStatus: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update KYC status: " + e.getMessage());
        }
    }


    public Customer updateCustomerPhone(Long customerId, String phone) {
        logger.info("Entering updateCustomerPhone with customerId: {} and phone: {}", customerId, phone);
        try {
            Customer customer = getCustomerById(customerId);

            if (phone == null || phone.isBlank()) {
                throw new RuntimeException("Phone cannot be empty");
            }

            customer.setPhone(phone);

            Customer savedCustomer = customerRepository.save(customer);
            logger.info("Customer phone updated for id: {}", savedCustomer.getId());
            return savedCustomer;
        } catch (Exception e) {
            logger.error("Error in updateCustomerPhone: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update phone: " + e.getMessage());
        }
    }


    public Customer updateCustomerEmail(Long customerId, String email) {
        logger.info("Entering updateCustomerEmail with customerId: {} and email: {}", customerId, email);
        try {
            Customer customer = getCustomerById(customerId);

            if (email == null || email.isBlank()) {
                throw new RuntimeException("Email cannot be empty");
            }

            customer.setEmail(email);

            Customer savedCustomer = customerRepository.save(customer);
            logger.info("Customer email updated for id: {}", savedCustomer.getId());
            return savedCustomer;
        } catch (Exception e) {
            logger.error("Error in updateCustomerEmail: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update email: " + e.getMessage());
        }
    }


    public Customer updateCustomerName(Long customerId, String name) {
        logger.info("Entering updateCustomerName with customerId: {} and name: {}", customerId, name);
        try {
            Customer customer = getCustomerById(customerId);

            if (name == null || name.isBlank()) {
                throw new RuntimeException("Name cannot be empty");
            }

            customer.setName(name);

            Customer savedCustomer = customerRepository.save(customer);
            logger.info("Customer name updated for id: {}", savedCustomer.getId());
            return savedCustomer;
        } catch (Exception e) {
            logger.error("Error in updateCustomerName: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update name: " + e.getMessage());
        }
    }

    public void deleteCustomer(Long customerId) {
        logger.info("Entering deleteCustomer with customerId: {}", customerId);
        try {
            Customer customer = getCustomerById(customerId);

            customerRepository.delete(customer);
            logger.info("Customer deleted with id: {}", customerId);
        } catch (Exception e) {
            logger.error("Error in deleteCustomer: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete customer: " + e.getMessage());
        }
    }

    public void uploadCustomersFromCsv(org.springframework.web.multipart.MultipartFile file) {
        logger.info("Entering uploadCustomersFromCsv");
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");
                // CSV columns: customer_id,name,email,phone,kyc_status,created_at
                if (data.length >= 6) {
                    Customer customer = new Customer();
                    // We can either set the ID or let it auto-generate, but since dataset has ID, we might just ignore the CSV ID to let MySQL auto-generate, 
                    // or set it if we want to retain the exact ID. For JPA to save with specific ID without thinking it's an update, we'd need to handle it.
                    // Let's assume we want to insert them as new, or update if exists. Since they might exist, let's try to find them first.
                    Long id = Long.parseLong(data[0].trim());
                    java.util.Optional<Customer> existing = customerRepository.findById(id);
                    if (existing.isPresent()) {
                        customer = existing.get();
                    } else {
                        customer.setId(id);
                    }
                    customer.setName(data[1].trim());
                    customer.setEmail(data[2].trim());
                    customer.setPhone(data[3].trim());
                    customer.setKycStatus(data[4].trim());
                    customer.setCreatedAt(LocalDateTime.parse(data[5].trim(), formatter));
                    
                    customerRepository.save(customer);
                }
            }
            logger.info("CSV upload completed successfully.");
        } catch (Exception e) {
            logger.error("Error in uploadCustomersFromCsv: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload customers from CSV: " + e.getMessage());
        }
    }
}