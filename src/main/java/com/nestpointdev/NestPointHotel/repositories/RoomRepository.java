package com.nestpointdev.NestPointHotel.repositories;

import com.nestpointdev.NestPointHotel.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCNT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> getAllAvailableRooms();
    @Query("SELECT r FROM r WHERE r.room.roomType LIKE %:roomType% AND r.id NOT IN (SELECT bk.room.id FROM Booking bk where (bk.checkInDate <= :bk.checkOutDate) AND (bk.checkOutDate >= :checkInDate))")
    List<Room> findAvailableRoomsByDateAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
