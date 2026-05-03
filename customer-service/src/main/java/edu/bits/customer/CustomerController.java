package edu.bits.customer;

import lombok.extern.slf4j.Slf4j;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository repository;
    private final Counter customerCrudCounter;

    public CustomerController(CustomerRepository repository, MeterRegistry meterRegistry) {
        this.repository = repository;
        this.customerCrudCounter = meterRegistry.counter("customer_crud_total");
    }

    @GetMapping
    public List<Customer> all() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Customer byId(@PathVariable Long id) {
        log.info("Fetching customer with id: {}", id);
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@RequestBody Customer customer) {
        log.info("Creating customer: {}, email: {}", customer.getName(), mask(customer.getEmail()));
        customerCrudCounter.increment();
        return repository.save(customer);
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer payload) {
        Customer customer = byId(id);
        customer.setName(payload.getName());
        customer.setEmail(payload.getEmail());
        customer.setPhone(payload.getPhone());
        customer.setKycStatus(payload.getKycStatus());
        customerCrudCounter.increment();
        return repository.save(customer);
    }

    @PatchMapping("/{id}/kyc")
    public Customer updateKyc(@PathVariable Long id, @RequestParam String status) {
        Customer customer = byId(id);
        customer.setKycStatus(status);
        customerCrudCounter.increment();
        return repository.save(customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Deleting customer with id: {}", id);
        repository.delete(byId(id));
        customerCrudCounter.increment();
    }

    private String mask(String s) {
        if (s == null || s.length() < 4) return "****";
        return s.substring(0, 2) + "****" + s.substring(s.length() - 2);
    }
}
