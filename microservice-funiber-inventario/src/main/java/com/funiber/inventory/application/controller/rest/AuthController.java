package com.funiber.inventory.application.controller.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import com.funiber.inventory.application.dto.request.LoginDTO;
import com.funiber.inventory.application.dto.request.RegisterDTO;
import com.funiber.inventory.application.dto.response.SignInDTO;
import com.funiber.inventory.application.dto.response.StandardResponse;
import com.funiber.inventory.application.dto.response.UserRespDTO;
import com.funiber.inventory.application.service.JwtService;
import com.funiber.inventory.domain.dto.User;
import com.funiber.inventory.domain.exception.CategoryExistException;
import com.funiber.inventory.domain.exception.UserNotFoundException;
import com.funiber.inventory.domain.service.UserService;

@RestController
@RequestMapping("/api/user-management")
public class AuthController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtService jwtService;
    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO request) {
        StandardResponse<SignInDTO> response = new StandardResponse<>();
        try {
            User user = userService.findUserByEmail(request.getEmail());
            if (user != null && userService.validatePassword(user, request.getPassword())) {
                String token = jwtService.generateToken(user);
                SignInDTO signInDTO = new SignInDTO();
                signInDTO.setUser(modelMapper.map(user, UserRespDTO.class));
                signInDTO.setToken(token);
                response.setData(signInDTO);
                response.setHttpStatus(200);
                response.setMessage("Login successful");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.setMessage("Check Password or Email");
                response.setHttpStatus(401);
                response.setStatus("Not authorized");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (UserNotFoundException e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(404);
            response.setStatus("Not Found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDTO userRequest) {
        StandardResponse<?> response = new StandardResponse<>();
        try {
            User user = modelMapper.map(userRequest, User.class);
            if (userService.save(user)) {
                response.setMessage("Registro creado Exitosamente");
                response.setHttpStatus(201);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.setMessage("Registro no creado");
                response.setHttpStatus(500);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (CategoryExistException e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(404);
            response.setStatus("Not Found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setHttpStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
