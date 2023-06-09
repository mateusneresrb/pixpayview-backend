package dev.mateusneres.pixpayview.services;

import dev.mateusneres.pixpayview.dtos.request.UserSettingsRequest;
import dev.mateusneres.pixpayview.dtos.response.UserDetailsResponse;
import dev.mateusneres.pixpayview.entities.User;
import dev.mateusneres.pixpayview.enums.Role;
import dev.mateusneres.pixpayview.exceptions.AccountNotExistsException;
import dev.mateusneres.pixpayview.exceptions.EmailAlreadyExistsException;
import dev.mateusneres.pixpayview.exceptions.ForbiddenException;
import dev.mateusneres.pixpayview.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
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

    public ResponseEntity<Object> deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AccountNotExistsException(4000, "Account by id: " + id + " not exists!"));

        userRepository.delete(user);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<Object> updateSettings(String email, UUID userID, UserSettingsRequest userSettingsRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        Optional<User> optionalTargetUser = userRepository.findById(userID);

        if (optionalUser.isEmpty() || optionalTargetUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalTargetUser.get();
        if (!optionalUser.get().getUserID().equals(userID) && optionalUser.get().getRole() != Role.ROLE_ADMIN) {
            throw new ForbiddenException(7000, "You don't have enough permissions to change another user's settings.");
        }

        if (userRepository.existsUserByEmail(userSettingsRequest.getEmail())) {
            throw new EmailAlreadyExistsException(3001, "The email address " + userSettingsRequest.getEmail() + " is not available for use.");
        }

        if (userSettingsRequest.getEmail() != null) user.setEmail(userSettingsRequest.getEmail());
        if (userSettingsRequest.getName() != null) user.setName(userSettingsRequest.getName());
        if (userSettingsRequest.getRole() != null) user.setRole(userSettingsRequest.getRole());
        if (userSettingsRequest.getPassword() != null)
            user.setPassword(passwordEncoder.encode(userSettingsRequest.getPassword()));
        user.setUpdatedAt(new Timestamp(new Date().getTime()));

        userDetailsService.updateUserDetails(user);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
