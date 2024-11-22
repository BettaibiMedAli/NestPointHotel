package com.nestpointdev.NestPointHotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nestpointdev.NestPointHotel.models.Booking;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findBookingConfirmationCode(String confirmationCode);
}
