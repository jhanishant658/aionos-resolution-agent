package com.aionos.agent.repository;

import com.aionos.agent.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByBookingReference(String bookingReference);
    Optional<Customer> findByNameIgnoreCase(String name);
}
