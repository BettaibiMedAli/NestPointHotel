package com.nestpointdev.NestPointHotel.controllers;

import com.nestpointdev.NestPointHotel.dto.Response;
import com.nestpointdev.NestPointHotel.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers()
    {
        Response response = userService.getAllUsers();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/getById/{userId}")
    public ResponseEntity<Response> getUserById(@PathVariable("userId") String userId)
    {
        Response response = userService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable("userId") String userId)
    {
        Response response = userService.deleteUser(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/ProfileInfo/{userId}")
    public ResponseEntity<Response> getUserProfile()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Response response = userService.getUserById(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/userBookings/{userId}")
    public ResponseEntity<Response> getUserBookingHistory(@PathVariable("userId") String userId)
    {
        Response response = userService.getUserBookingHistory(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
