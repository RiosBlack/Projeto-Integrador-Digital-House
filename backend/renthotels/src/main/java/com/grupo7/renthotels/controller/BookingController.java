package com.grupo7.renthotels.controller;


import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.dto.BookingDTO;
import com.grupo7.renthotels.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> saveBooking(@RequestBody @Valid BookingDTO bookingDTO, @RequestHeader("Authorization") String token) throws NotFoundException {
        return bookingService.saveBooking(bookingDTO, token);
    }

    @GetMapping
    public List<BookingDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        return bookingService.deleteBooking(id);
    }

    @GetMapping(params = "bookingSku")
    public ResponseEntity<BookingDTO> getBookingBySku(@RequestParam(value = "bookingSku") Long bookingSku) {
        return bookingService.getBookingBySku(bookingSku);
    }

    @DeleteMapping(params = "bookingSku")
    public ResponseEntity<Void> deleteBookingBySku(@RequestParam(value = "bookingSku") Long sku) {
        return bookingService.deleteBookingBySku(sku);
    }

    @GetMapping(params = "userSku")
    public ResponseEntity<List<BookingDTO>> getBookingsByUser(@RequestParam(value = "userSku") Long userSku){
        return bookingService.getByUserSku(userSku);
    }

    @GetMapping(params = "productSku")
    public ResponseEntity<List<BookingDTO>> getBookingsByProduct(@RequestParam(value = "productSku") Long productSku){
        return bookingService.getByProductSku(productSku);
    }
}
