package com.nestpointdev.NestPointHotel.controllers;

import com.nestpointdev.NestPointHotel.dto.Response;
import com.nestpointdev.NestPointHotel.models.Booking;
import com.nestpointdev.NestPointHotel.services.interfaces.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private IBookingService bookingService;

    @PostMapping("/bookRoom/{roomId}/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Response> bookRoom(@PathVariable()Long roomId,
                                            @PathVariable() Long userId,
                                            @RequestBody Booking booking){
        Response response = bookingService.saveBooking(roomId, userId, booking);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllBookings(){
        Response response = bookingService.getAllBookings();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/byConfirmationCode/{confirmationCode}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        Response response = bookingService.findBookingByConfirmationCode(confirmationCode);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/cancel/{bookingId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> cancelBooking(@PathVariable Long bookingId){
        Response response = bookingService.cancelBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
