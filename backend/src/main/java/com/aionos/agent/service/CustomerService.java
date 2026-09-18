package com.aionos.agent.service;

import com.aionos.agent.entity.Customer;
import com.aionos.agent.exception.ResourceNotFoundException;
import com.aionos.agent.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByPnr(String pnr) {
        return customerRepository.findByBookingReference(pnr.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with booking reference " + pnr + " not found."));
    }

    public Customer getCustomerByName(String name) {
        return customerRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Customer " + name + " not found."));
    }
}