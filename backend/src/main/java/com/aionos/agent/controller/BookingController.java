package com.aionos.agent.controller;

import com.aionos.agent.entity.Booking;
import com.aionos.agent.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{pnr}")
    public ResponseEntity<List<Booking>> getBookingsByPnr(@PathVariable String pnr) {
        return ResponseEntity.ok(bookingService.getBookingsByPnr(pnr));
    }
}