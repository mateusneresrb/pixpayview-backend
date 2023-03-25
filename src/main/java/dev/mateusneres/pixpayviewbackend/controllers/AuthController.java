package dev.mateusneres.pixpayviewbackend.controllers;

import dev.mateusneres.pixpayviewbackend.dtos.request.LoginRequest;
import dev.mateusneres.pixpayviewbackend.dtos.request.SignupRequest;
import dev.mateusneres.pixpayviewbackend.dtos.response.SignUpResponse;
import dev.mateusneres.pixpayviewbackend.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticate(loginRequest);
    }

    @PostMapping(value = "/refreshtoken")
    public ResponseEntity<Object> refreshAndGetAuthenticationToken(@Valid @RequestBody LoginRequest loginRequest) {

        return null;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<SignUpResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(userService.register(signupRequest));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<Object> logoutUser(@Valid @RequestBody LoginRequest loginRequest) {

        return null;
    }
}
