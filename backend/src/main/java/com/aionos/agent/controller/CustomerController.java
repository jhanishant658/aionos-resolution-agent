package com.aionos.agent.controller;

import com.aionos.agent.entity.Customer;
import com.aionos.agent.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{pnr}")
    public ResponseEntity<Customer> getCustomerByPnr(@PathVariable String pnr) {
        return ResponseEntity.ok(customerService.getCustomerByPnr(pnr));
    }
}