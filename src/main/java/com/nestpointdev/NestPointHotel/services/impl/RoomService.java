package com.nestpointdev.NestPointHotel.services.impl;

import com.nestpointdev.NestPointHotel.dto.Response;
import com.nestpointdev.NestPointHotel.dto.RoomDTO;
import com.nestpointdev.NestPointHotel.dto.UserDTO;
import com.nestpointdev.NestPointHotel.exceptions.MyException;
import com.nestpointdev.NestPointHotel.models.Room;
import com.nestpointdev.NestPointHotel.models.User;
import com.nestpointdev.NestPointHotel.repositories.BookingRepository;
import com.nestpointdev.NestPointHotel.repositories.RoomRepository;
import com.nestpointdev.NestPointHotel.services.ImageStorageService;
import com.nestpointdev.NestPointHotel.services.interfaces.IRoomService;
import com.nestpointdev.NestPointHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ImageStorageService imageStorageService;

    @Override
    public Response addNewRoom(MultipartFile image, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();

        try {
            String imageUrl = imageStorageService.saveImage(image);
            Room room = new Room();
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomDescription(room.getRoomDescription());

            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);

            response.setRoom(roomDTO);
            response.setMessage("Successful");
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding the room " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);


            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all rooms " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();

        try {
            roomRepository.findById(roomId).orElseThrow(() -> new MyException("Room not found!"));
            roomRepository.deleteById(roomId);

            response.setMessage("Successful");
            response.setStatusCode(200);
        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile image) {
        Response response = new Response();

        try {
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = imageStorageService.saveImage(image);
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyException("Room not found!"));
            if (roomType != null) room.setRoomType(roomType);
            if (roomPrice != null) room.setRoomPrice(roomPrice);
            if (description != null) room.setRoomType(description);
            if (imageUrl != null) room.setRoomType(imageUrl);

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setRoom(roomDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating a room " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyException("Room not found!"));
            RoomDTO roomDTO = Utils.mapRoomEntitytoRoomDTOPlusBookings(room);
            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setRoom(roomDTO);
        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting room by id " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRoomsbyDateAndType(LocalDate checkInData, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDateAndTypes(checkInData, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(availableRooms);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting available rooms " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllAvailableRooms() {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.getAllAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setRoomList(roomDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting available rooms " + e.getMessage());
        }
        return response;
    }
}
