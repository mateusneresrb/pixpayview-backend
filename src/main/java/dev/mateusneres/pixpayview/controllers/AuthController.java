package dev.mateusneres.pixpayview.controllers;

import dev.mateusneres.pixpayview.dtos.request.LoginRequest;
import dev.mateusneres.pixpayview.dtos.request.SignupRequest;
import dev.mateusneres.pixpayview.dtos.response.UserDetailsResponse;
import dev.mateusneres.pixpayview.security.jwt.JwtTokenUtil;
import dev.mateusneres.pixpayview.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }

    @PostMapping(value = "/refreshtoken")
    public ResponseEntity<Object> refreshAndGetAuthenticationToken(@Valid @RequestBody LoginRequest loginRequest) {

        return null;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<UserDetailsResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(authService.register(signupRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        jwtTokenUtil.removeToken(token);
        return ResponseEntity.ok().build();
    }

}
