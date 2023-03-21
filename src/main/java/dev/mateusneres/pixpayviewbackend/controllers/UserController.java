package dev.mateusneres.pixpayviewbackend.controllers;

import dev.mateusneres.pixpayviewbackend.dtos.request.UserSettingsRequest;
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

@RestController
@AllArgsConstructor
@RequestMapping("/v1/")
public class UserController {

    private UserService userService;

    @PutMapping(value = "/user/settings")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody() UserSettingsRequest userSettingsRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(1, "Request inválido");
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/user/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String userID) {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/user/list")
    public ResponseEntity<Object> listUsers() {

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

