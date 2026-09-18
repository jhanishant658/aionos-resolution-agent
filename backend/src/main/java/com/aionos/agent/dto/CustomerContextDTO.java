package com.aionos.agent.dto;

import com.aionos.agent.entity.Booking;
import com.aionos.agent.entity.Customer;
import com.aionos.agent.entity.Flight;
import java.util.List;

public class CustomerContextDTO {
    private Customer customer;
    private List<Booking> bookings;
    private Flight activeFlight;

    public CustomerContextDTO() {}

    public CustomerContextDTO(Customer customer, List<Booking> bookings, Flight activeFlight) {
        this.customer = customer;
        this.bookings = bookings;
        this.activeFlight = activeFlight;
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }

    public Flight getActiveFlight() { return activeFlight; }
    public void setActiveFlight(Flight activeFlight) { this.activeFlight = activeFlight; }
}