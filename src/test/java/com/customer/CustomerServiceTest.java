package com.customer;

import com.customer.entity.Customer;
import com.customer.repository.CustomerRepository;
import com.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private com.customer.service.CustomerEventProducer eventProducer;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test User");
        customer.setEmail("test@example.com");
        customer.setPhone("1234567890");
        customer.setKycStatus("VERIFIED");
        customer.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Customer result = customerService.getCustomerById(1L);
        assertNotNull(result);
        assertEquals("Test User", result.getName());
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));
        List<Customer> result = customerService.getAllCustomers();
        assertEquals(1, result.size());
    }

    @Test
    void testCreateCustomer() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        Customer newCustomer = new Customer();
        newCustomer.setName("New User");
        Customer result = customerService.createCustomer(newCustomer);
        assertNotNull(result);
        assertEquals("VERIFIED", result.getKycStatus()); // as per mock
    }

    @Test
    void testUploadCustomersFromCsv() throws Exception {
        String csvContent = "customer_id,name,email,phone,kyc_status,created_at\n" +
                "1,Vivaan Khan,vivaan.khan90@inbox.com,9288353015,VERIFIED,2022-09-12 10:02:36";
        MockMultipartFile file = new MockMultipartFile("file", "customers.csv", "text/csv", csvContent.getBytes());
        
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        assertDoesNotThrow(() -> customerService.uploadCustomersFromCsv(file));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}
