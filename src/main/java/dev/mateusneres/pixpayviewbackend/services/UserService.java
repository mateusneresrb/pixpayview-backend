package dev.mateusneres.pixpayviewbackend.services;

import dev.mateusneres.pixpayviewbackend.dtos.request.LoginRequest;
import dev.mateusneres.pixpayviewbackend.dtos.request.SignupRequest;
import dev.mateusneres.pixpayviewbackend.dtos.response.LoginResponse;
import dev.mateusneres.pixpayviewbackend.dtos.response.SignUpResponse;
import dev.mateusneres.pixpayviewbackend.entities.User;
import dev.mateusneres.pixpayviewbackend.exceptions.AccountAlreadyExistsException;
import dev.mateusneres.pixpayviewbackend.repositories.UserRepository;
import dev.mateusneres.pixpayviewbackend.security.jwt.JwtTokenUtil;
import dev.mateusneres.pixpayviewbackend.security.jwt.JwtUserDetails;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    public ResponseEntity<Object> authenticate(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            User user = getUserByEmail(loginRequest.getEmail());
            String jwtToken = jwtTokenUtil.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(jwtToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials!");
        }
    }

    public SignUpResponse register(SignupRequest signupRequest) {
        if (existsUserByEmail(signupRequest.getEmail())) {
            throw new AccountAlreadyExistsException(301, "The email: " + signupRequest.getEmail() + " address you entered is already registered.");
        }

        User user = new User(
                signupRequest.getEmail(),
                signupRequest.getName(),
                signupRequest.getRole(),
                passwordEncoder.encode(signupRequest.getPassword()),
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()));

        save(user);

        SignUpResponse signUpResponse = new SignUpResponse();
        BeanUtils.copyProperties(user, signUpResponse);

        return signUpResponse;
    }

    public User getUserByEmail(String email) {
        if (!existsUserByEmail(email)) return null;

        return userRepository.findByEmail(email);
    }


    public UserDetails loadUserByEmail(String email) {
        User user = getUserByEmail(email);

        //TODO REFACTOR TO CLEAN CODE
        return new JwtUserDetails(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }


}
