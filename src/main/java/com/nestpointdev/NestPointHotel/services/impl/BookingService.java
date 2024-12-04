package com.nestpointdev.NestPointHotel.services.impl;

import com.nestpointdev.NestPointHotel.dto.BookingDTO;
import com.nestpointdev.NestPointHotel.dto.Response;
import com.nestpointdev.NestPointHotel.dto.RoomDTO;
import com.nestpointdev.NestPointHotel.exceptions.MyException;
import com.nestpointdev.NestPointHotel.models.Booking;
import com.nestpointdev.NestPointHotel.models.Room;
import com.nestpointdev.NestPointHotel.models.User;
import com.nestpointdev.NestPointHotel.repositories.BookingRepository;
import com.nestpointdev.NestPointHotel.repositories.RoomRepository;
import com.nestpointdev.NestPointHotel.repositories.UserRepository;
import com.nestpointdev.NestPointHotel.services.interfaces.IBookingService;
import com.nestpointdev.NestPointHotel.services.interfaces.IRoomService;
import com.nestpointdev.NestPointHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService implements IBookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response = new Response();

        try {
            if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
                throw new IllegalArgumentException("CheckIn date must be before CheckOut date!");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new MyException("Room not found!"));
            User user = userRepository.findById(userId).orElseThrow(() -> new MyException("user not found!"));
            List<Booking> existingBookings = room.getBookings();
            if (!roomIsAvailable(bookingRequest, existingBookings)) {
                throw new MyException("Room not available for this selected date!");
            }
            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
            bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
            bookingRepository.save(bookingRequest);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setBookingConfirmationCode(bookingConfirmationCode);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a booking " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response findBookingByConfirmationCode(String ConfirmationCode) {
        Response response = new Response();

        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(ConfirmationCode).orElseThrow(()->new MyException("Booking not found!"));
            BookingDTO bookingDTO = Utils.mapBookingEntitytoBookingDTOPlusBookedRooms(booking, true);
            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setBooking(bookingDTO);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting booking by confirmation code! " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();

        try {
            List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
            List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setBookingList(bookingDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings! " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();

        try {
            bookingRepository.findById(bookingId).orElseThrow(()-> new MyException("Booking not found!"));
            bookingRepository.deleteById(bookingId);

            response.setMessage("Successful");
            response.setStatusCode(200);
        }catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all bookings! " + e.getMessage());
        }
        return response;
    }

    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckInDate()))
                );
    }

}
