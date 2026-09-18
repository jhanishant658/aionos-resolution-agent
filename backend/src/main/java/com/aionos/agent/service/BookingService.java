package com.aionos.agent.service;

import com.aionos.agent.entity.Booking;
import com.aionos.agent.entity.Flight;
import com.aionos.agent.exception.ResourceNotFoundException;
import com.aionos.agent.repository.BookingRepository;
import com.aionos.agent.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    public BookingService(BookingRepository bookingRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    public List<Booking> getBookingsByPnr(String pnr) {
        List<Booking> bookings = bookingRepository.findByPnr(pnr.trim().toUpperCase());
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No bookings found for PNR: " + pnr);
        }
        return bookings;
    }

    public Booking getPrimaryBookingByPnr(String pnr) {
        return bookingRepository.findFirstByPnr(pnr.trim().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Booking for PNR " + pnr + " not found."));
    }

    public Flight getFlightByNumber(String flightNumber) {
        return flightRepository.findByFlightNumber(flightNumber.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Flight " + flightNumber + " not found."));
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
}