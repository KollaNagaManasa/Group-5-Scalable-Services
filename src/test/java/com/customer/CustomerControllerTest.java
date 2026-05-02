package com.customer;

import com.customer.controller.CustomerController;
import com.customer.dto.CustomerRequest;
import com.customer.entity.Customer;
import com.customer.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer customer;
    private CustomerRequest customerRequest;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test User");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        customer.setKycStatus("VERIFIED");
        customer.setCreatedAt(LocalDateTime.now());

        customerRequest = new CustomerRequest();
        customerRequest.setName("Test User");
        customerRequest.setEmail("test@example.com");
        customerRequest.setPhone("1234567890");
    }

    @Test
    void testGetCustomer() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer));

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test User"));
    }

    @Test
    void testCreateCustomer() throws Exception {
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void testUploadCustomers() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "customers.csv", "text/csv", "dummy".getBytes());
        doNothing().when(customerService).uploadCustomersFromCsv(any());

        mockMvc.perform(multipart("/api/v1/customers/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Customers uploaded successfully from CSV."));
    }
}
