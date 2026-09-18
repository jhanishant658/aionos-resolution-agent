package com.aionos.agent.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pnr;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(nullable = false)
    private String bookingStatus;

    @Column(nullable = false)
    private String originalPaymentMethod;

    private Double fareAmount;

    private String currency;

    public Booking() {}

    public Booking(Long id, String pnr, Customer customer, Flight flight,
                   String bookingStatus, String originalPaymentMethod, Double fareAmount, String currency) {
        this.id = id;
        this.pnr = pnr;
        this.customer = customer;
        this.flight = flight;
        this.bookingStatus = bookingStatus;
        this.originalPaymentMethod = originalPaymentMethod;
        this.fareAmount = fareAmount;
        this.currency = currency;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String pnr;
        private Customer customer;
        private Flight flight;
        private String bookingStatus;
        private String originalPaymentMethod;
        private Double fareAmount;
        private String currency;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder pnr(String pnr) { this.pnr = pnr; return this; }
        public Builder customer(Customer customer) { this.customer = customer; return this; }
        public Builder flight(Flight flight) { this.flight = flight; return this; }
        public Builder bookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; return this; }
        public Builder originalPaymentMethod(String originalPaymentMethod) { this.originalPaymentMethod = originalPaymentMethod; return this; }
        public Builder fareAmount(Double fareAmount) { this.fareAmount = fareAmount; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }
        public Booking build() {
            return new Booking(id, pnr, customer, flight, bookingStatus, originalPaymentMethod, fareAmount, currency);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public String getOriginalPaymentMethod() { return originalPaymentMethod; }
    public void setOriginalPaymentMethod(String originalPaymentMethod) { this.originalPaymentMethod = originalPaymentMethod; }

    public Double getFareAmount() { return fareAmount; }
    public void setFareAmount(Double fareAmount) { this.fareAmount = fareAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}