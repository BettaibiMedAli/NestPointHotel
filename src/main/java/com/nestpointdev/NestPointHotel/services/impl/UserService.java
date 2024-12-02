package com.nestpointdev.NestPointHotel.services.impl;

import com.nestpointdev.NestPointHotel.dto.LoginRequest;
import com.nestpointdev.NestPointHotel.dto.Response;
import com.nestpointdev.NestPointHotel.dto.UserDTO;
import com.nestpointdev.NestPointHotel.exceptions.MyException;
import com.nestpointdev.NestPointHotel.models.User;
import com.nestpointdev.NestPointHotel.repositories.UserRepository;
import com.nestpointdev.NestPointHotel.services.interfaces.IUserService;
import com.nestpointdev.NestPointHotel.utils.JWTUtils;
import com.nestpointdev.NestPointHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response register(User user) {
        Response response = new Response();

        try{
            if(user.getRole() == null || user.getRole().isBlank()) user.setRole("USER");
            if(userRepository.existsByEmail(user.getEmail()))
            {
                throw new MyException(user.getEmail() + " Already exists" );
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);

            response.setStatusCode(200);
            response.setUser(userDTO);


        }catch (MyException e)
        {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());

        }catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error saving a user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user  = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new MyException("User Not Found!"));
            var token = jwtUtils.generateToken(user);
            response.setToken(token);
            response.setExpirationTime("7 days");
            response.setRole(user.getRole());
            response.setMessage("Successful");


            response.setStatusCode(200);


        }catch (MyException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error Logging In the user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try{
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setUserList(userDTOList);
            response.setMessage("Successful");


            response.setStatusCode(200);


        }catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error getting all users " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response = new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User Not Found!"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setUser(userDTO);

        }catch (MyException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error getting user bookings " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User Not Found!"));
            userRepository.deleteById(Long.valueOf(userId));

            response.setMessage("Successful");
            response.setStatusCode(200);

        }catch (MyException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error deleting a user " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try{
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User Not Found!"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setUser(userDTO);

        }catch (MyException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error getting a user by ID! " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try{
            User user = userRepository.findByEmail(email).orElseThrow(() -> new MyException("User Not Found!"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setMessage("Successful");
            response.setStatusCode(200);
            response.setUser(userDTO);
        }catch (MyException e)
        {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        }catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error getting the user info " + e.getMessage());
        }
        return response;
    }
}
