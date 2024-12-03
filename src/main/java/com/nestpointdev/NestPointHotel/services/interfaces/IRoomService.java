package com.nestpointdev.NestPointHotel.services.interfaces;

import com.nestpointdev.NestPointHotel.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    Response addNewRoom(MultipartFile image, String roomType, BigDecimal roomPrice, String description);
    List<String> getAllRoomTypes();
    Response getAllRooms();
    Response deleteRoom(Long roomId);
    Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile image);
    Response getRoomById(Long roomId);
    Response getAvailableRoomsbyDateAndType(LocalDate checkInData, LocalDate checkOutDate, String roomType);
    Response getAllAvailableRooms();
}
