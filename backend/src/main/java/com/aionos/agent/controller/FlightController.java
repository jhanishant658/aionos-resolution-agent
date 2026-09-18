package com.aionos.agent.controller;

import com.aionos.agent.entity.Flight;
import com.aionos.agent.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final BookingService bookingService;

    public FlightController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.ok(bookingService.getAllFlights());
    }

    @GetMapping("/{flightNumber}")
    public ResponseEntity<Flight> getFlightByNumber(@PathVariable String flightNumber) {
        return ResponseEntity.ok(bookingService.getFlightByNumber(flightNumber));
    }
}