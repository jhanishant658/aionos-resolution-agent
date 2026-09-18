package com.aionos.agent.entity;

import com.aionos.agent.enums.LoyaltyTier;
import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoyaltyTier loyaltyTier;

    @Column(nullable = false, unique = true)
    private String bookingReference;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String maskedPhone;

    private int travelHistoryFlightsCount;

    @Column(length = 1000)
    private String priorComplaints;

    public Customer() {}

    public Customer(Long id, String name, LoyaltyTier loyaltyTier, String bookingReference,
                    String email, String maskedPhone, int travelHistoryFlightsCount, String priorComplaints) {
        this.id = id;
        this.name = name;
        this.loyaltyTier = loyaltyTier;
        this.bookingReference = bookingReference;
        this.email = email;
        this.maskedPhone = maskedPhone;
        this.travelHistoryFlightsCount = travelHistoryFlightsCount;
        this.priorComplaints = priorComplaints;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private LoyaltyTier loyaltyTier;
        private String bookingReference;
        private String email;
        private String maskedPhone;
        private int travelHistoryFlightsCount;
        private String priorComplaints;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder loyaltyTier(LoyaltyTier loyaltyTier) { this.loyaltyTier = loyaltyTier; return this; }
        public Builder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder maskedPhone(String maskedPhone) { this.maskedPhone = maskedPhone; return this; }
        public Builder travelHistoryFlightsCount(int travelHistoryFlightsCount) { this.travelHistoryFlightsCount = travelHistoryFlightsCount; return this; }
        public Builder priorComplaints(String priorComplaints) { this.priorComplaints = priorComplaints; return this; }
        public Customer build() {
            return new Customer(id, name, loyaltyTier, bookingReference, email, maskedPhone, travelHistoryFlightsCount, priorComplaints);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LoyaltyTier getLoyaltyTier() { return loyaltyTier; }
    public void setLoyaltyTier(LoyaltyTier loyaltyTier) { this.loyaltyTier = loyaltyTier; }

    public String getBookingReference() { return bookingReference; }
    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMaskedPhone() { return maskedPhone; }
    public void setMaskedPhone(String maskedPhone) { this.maskedPhone = maskedPhone; }

    public int getTravelHistoryFlightsCount() { return travelHistoryFlightsCount; }
    public void setTravelHistoryFlightsCount(int travelHistoryFlightsCount) { this.travelHistoryFlightsCount = travelHistoryFlightsCount; }

    public String getPriorComplaints() { return priorComplaints; }
    public void setPriorComplaints(String priorComplaints) { this.priorComplaints = priorComplaints; }
}