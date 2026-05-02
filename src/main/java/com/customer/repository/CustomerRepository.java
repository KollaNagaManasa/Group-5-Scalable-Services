package com.customer.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.customer.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}