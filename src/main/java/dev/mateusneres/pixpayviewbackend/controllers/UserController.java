package dev.mateusneres.pixpayviewbackend.controllers;

import dev.mateusneres.pixpayviewbackend.dtos.request.UserSettingsRequest;
import dev.mateusneres.pixpayviewbackend.dtos.response.UserDetailsResponse;
import dev.mateusneres.pixpayviewbackend.entities.User;
import dev.mateusneres.pixpayviewbackend.exceptions.BadRequestException;
import dev.mateusneres.pixpayviewbackend.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @PutMapping(value = "/settings")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody() UserSettingsRequest userSettingsRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(1, "Request inválido");
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String userID) {
        try {
            UUID uuid = UUID.fromString(userID);

            return userService.deleteUser(uuid);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(1, "Request inválido");
        }
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<UserDetailsResponse>> listUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

}

