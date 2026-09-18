package com.aionos.agent.entity;

import com.aionos.agent.enums.FlightStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String flightNumber;

    @Column(nullable = false)
    private String routeFrom;

    @Column(nullable = false)
    private String routeTo;

    @Column(nullable = false)
    private String flightDate;

    @Column(nullable = false)
    private String scheduledDeparture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status;

    private Integer delayHours;

    private String newDeparture;

    private String cancellationReason;

    public Flight() {}

    public Flight(Long id, String flightNumber, String routeFrom, String routeTo,
                  String flightDate, String scheduledDeparture, FlightStatus status,
                  Integer delayHours, String newDeparture, String cancellationReason) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.routeFrom = routeFrom;
        this.routeTo = routeTo;
        this.flightDate = flightDate;
        this.scheduledDeparture = scheduledDeparture;
        this.status = status;
        this.delayHours = delayHours;
        this.newDeparture = newDeparture;
        this.cancellationReason = cancellationReason;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String flightNumber;
        private String routeFrom;
        private String routeTo;
        private String flightDate;
        private String scheduledDeparture;
        private FlightStatus status;
        private Integer delayHours;
        private String newDeparture;
        private String cancellationReason;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder flightNumber(String flightNumber) { this.flightNumber = flightNumber; return this; }
        public Builder routeFrom(String routeFrom) { this.routeFrom = routeFrom; return this; }
        public Builder routeTo(String routeTo) { this.routeTo = routeTo; return this; }
        public Builder flightDate(String flightDate) { this.flightDate = flightDate; return this; }
        public Builder scheduledDeparture(String scheduledDeparture) { this.scheduledDeparture = scheduledDeparture; return this; }
        public Builder status(FlightStatus status) { this.status = status; return this; }
        public Builder delayHours(Integer delayHours) { this.delayHours = delayHours; return this; }
        public Builder newDeparture(String newDeparture) { this.newDeparture = newDeparture; return this; }
        public Builder cancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; return this; }
        public Flight build() {
            return new Flight(id, flightNumber, routeFrom, routeTo, flightDate, scheduledDeparture, status, delayHours, newDeparture, cancellationReason);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getRouteFrom() { return routeFrom; }
    public void setRouteFrom(String routeFrom) { this.routeFrom = routeFrom; }

    public String getRouteTo() { return routeTo; }
    public void setRouteTo(String routeTo) { this.routeTo = routeTo; }

    public String getFlightDate() { return flightDate; }
    public void setFlightDate(String flightDate) { this.flightDate = flightDate; }

    public String getScheduledDeparture() { return scheduledDeparture; }
    public void setScheduledDeparture(String scheduledDeparture) { this.scheduledDeparture = scheduledDeparture; }

    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }

    public Integer getDelayHours() { return delayHours; }
    public void setDelayHours(Integer delayHours) { this.delayHours = delayHours; }

    public String getNewDeparture() { return newDeparture; }
    public void setNewDeparture(String newDeparture) { this.newDeparture = newDeparture; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
}