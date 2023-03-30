package dev.mateusneres.pixpayviewbackend.services;

import dev.mateusneres.pixpayviewbackend.dtos.request.LoginRequest;
import dev.mateusneres.pixpayviewbackend.dtos.request.SignupRequest;
import dev.mateusneres.pixpayviewbackend.dtos.response.LoginResponse;
import dev.mateusneres.pixpayviewbackend.dtos.response.UserDetailsResponse;
import dev.mateusneres.pixpayviewbackend.entities.User;
import dev.mateusneres.pixpayviewbackend.exceptions.AccountAlreadyExistsException;
import dev.mateusneres.pixpayviewbackend.repositories.UserRepository;
import dev.mateusneres.pixpayviewbackend.security.jwt.JwtTokenUtil;
import dev.mateusneres.pixpayviewbackend.security.jwt.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<Object> authenticate(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            UserDetails userDetails = inMemoryUserDetailsManager.loadUserByUsername(loginRequest.getEmail());
            String jwtToken = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponse(jwtToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials!");
        }
    }

    public UserDetailsResponse register(SignupRequest signupRequest) {
        if (userRepository.existsUserByEmail(signupRequest.getEmail())) {
            throw new AccountAlreadyExistsException(3000, "The email: " + signupRequest.getEmail() + " address you entered is already registered.");
        }

        User user = new User(
                signupRequest.getEmail(),
                signupRequest.getName(),
                signupRequest.getRole(),
                passwordEncoder.encode(signupRequest.getPassword()),
                new Timestamp(new Date().getTime()),
                new Timestamp(new Date().getTime()));

        userRepository.save(user);

        JwtUserDetails jwtUserDetails = new JwtUserDetails(
                user.getUserID(),
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
        inMemoryUserDetailsManager.createUser(jwtUserDetails);

        UserDetailsResponse signUpResponse = new UserDetailsResponse();
        BeanUtils.copyProperties(user, signUpResponse);

        return signUpResponse;
    }

}
