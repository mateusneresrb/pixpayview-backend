package dev.mateusneres.pixpayview.controllers;

import dev.mateusneres.pixpayview.dtos.request.UserSettingsRequest;
import dev.mateusneres.pixpayview.dtos.response.UserDetailsResponse;
import dev.mateusneres.pixpayview.exceptions.BadRequestException;
import dev.mateusneres.pixpayview.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Update a user settings")
    @PutMapping(value = "/{id}/settings")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody() UserSettingsRequest userSettingsRequest, Authentication authentication, BindingResult bindingResult, @PathVariable("id") String userID) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(5000, "The request sent is invalid, check a application documentation.");
        }

        try {
            UUID uuid = UUID.fromString(userID);

            return userService.updateSettings(authentication.getName(), uuid, userSettingsRequest);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(5000, "The request sent is invalid, check a application documentation.");
        }
    }

    @Operation(summary = "Delete a user")
    @DeleteMapping(value = "/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String userID) {
        try {
            UUID uuid = UUID.fromString(userID);

            return userService.deleteUser(uuid);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(5000, "The request sent is invalid, check a application documentation.");
        }
    }

    @Operation(summary = "List all users")
    @GetMapping(value = "/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDetailsResponse>> listUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

}

