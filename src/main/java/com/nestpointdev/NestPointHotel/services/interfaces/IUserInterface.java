package com.nestpointdev.NestPointHotel.services.interfaces;

import com.nestpointdev.NestPointHotel.dto.LoginRequest;
import com.nestpointdev.NestPointHotel.dto.Response;
import com.nestpointdev.NestPointHotel.models.User;

public interface IUserInterface {
    Response register(User user);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserBookingHistory(String userId);
    Response deleteUser(String userId);
    Response getUserById(String userId);
    Response getMyInfo(String email);

}
