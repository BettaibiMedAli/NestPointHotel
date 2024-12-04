package com.nestpointdev.NestPointHotel.services.interfaces;

import com.nestpointdev.NestPointHotel.dto.Response;
import com.nestpointdev.NestPointHotel.models.Booking;

public interface IBookingService {
    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);

    Response findBookingByConfirmationCode(String ConfirmationCode);

    Response getAllBookings();

    Response cancelBooking(Long bookingId);

}
