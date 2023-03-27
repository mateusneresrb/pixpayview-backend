package dev.mateusneres.pixpayviewbackend.controllers;

import dev.mateusneres.pixpayviewbackend.dtos.request.UserSettingsRequest;
import dev.mateusneres.pixpayviewbackend.dtos.response.UserDetailsResponse;
import dev.mateusneres.pixpayviewbackend.exceptions.BadRequestException;
import dev.mateusneres.pixpayviewbackend.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @PutMapping(value = "/{id}/settings")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody() UserSettingsRequest userSettingsRequest, Authentication authentication, BindingResult bindingResult, @PathVariable("id") String userID) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(1, "Request inválido");
        }

        try {
            UUID uuid = UUID.fromString(userID);

            return userService.updateSettings(authentication.getName(), uuid, userSettingsRequest);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(1, "Request inválido");
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String userID) {
        try {
            UUID uuid = UUID.fromString(userID);

            return userService.deleteUser(uuid);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(1, "Request inválido");
        }
    }

    @GetMapping(value = "/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDetailsResponse>> listUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

}

