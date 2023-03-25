package dev.mateusneres.pixpayviewbackend.services;

import dev.mateusneres.pixpayviewbackend.dtos.response.UserDetailsResponse;
import dev.mateusneres.pixpayviewbackend.entities.User;
import dev.mateusneres.pixpayviewbackend.repositories.UserRepository;
import dev.mateusneres.pixpayviewbackend.security.jwt.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDetailsResponse> findAll() {
        List<User> allUsers = userRepository.findAll();
        List<UserDetailsResponse> usersResponse = new ArrayList<>();

        allUsers.forEach(user -> {
            UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
            BeanUtils.copyProperties(user, userDetailsResponse);
            usersResponse.add(userDetailsResponse);
        });

        return usersResponse;
    }

//    public UserDetails loadUserByEmail(String email) {
//        User user = getUserByEmail(email);
//
//        //TODO REFACTOR TO CLEAN CODE
//        return new JwtUserDetails(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
//    }

}
