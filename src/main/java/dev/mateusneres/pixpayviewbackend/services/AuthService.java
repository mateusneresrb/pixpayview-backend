package dev.mateusneres.pixpayviewbackend.services;

import dev.mateusneres.pixpayviewbackend.dtos.request.LoginRequest;
import dev.mateusneres.pixpayviewbackend.dtos.request.SignupRequest;
import dev.mateusneres.pixpayviewbackend.dtos.response.LoginResponse;
import dev.mateusneres.pixpayviewbackend.dtos.response.UserDetailsResponse;
import dev.mateusneres.pixpayviewbackend.entities.User;
import dev.mateusneres.pixpayviewbackend.exceptions.AccountAlreadyExistsException;
import dev.mateusneres.pixpayviewbackend.security.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Object> authenticate(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            User user = userService.getUserByEmail(loginRequest.getEmail());
            String jwtToken = jwtTokenUtil.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(jwtToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials!");
        }
    }

    public UserDetailsResponse register(SignupRequest signupRequest) {
        if (userService.existsUserByEmail(signupRequest.getEmail())) {
            throw new AccountAlreadyExistsException(301, "The email: " + signupRequest.getEmail() + " address you entered is already registered.");
        }

        User user = new User(
                signupRequest.getEmail(),
                signupRequest.getName(),
                signupRequest.getRole(),
                passwordEncoder.encode(signupRequest.getPassword()),
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()));

        userService.save(user);

        UserDetailsResponse signUpResponse = new UserDetailsResponse();
        BeanUtils.copyProperties(user, signUpResponse);

        return signUpResponse;
    }

}
