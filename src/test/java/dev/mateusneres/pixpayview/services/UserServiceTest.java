package dev.mateusneres.pixpayview.services;

import dev.mateusneres.pixpayview.dtos.request.UserSettingsRequest;
import dev.mateusneres.pixpayview.dtos.response.UserDetailsResponse;
import dev.mateusneres.pixpayview.entities.User;
import dev.mateusneres.pixpayview.enums.Role;
import dev.mateusneres.pixpayview.exceptions.AccountNotExistsException;
import dev.mateusneres.pixpayview.exceptions.EmailAlreadyExistsException;
import dev.mateusneres.pixpayview.exceptions.ForbiddenException;
import dev.mateusneres.pixpayview.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;

    private User user2;

    @Before
    public void setUp() {
        user1 = new User("admin@example.com", "Admin User", Role.ROLE_ADMIN, "password", Timestamp.from(Instant.now()));
        user1.setUserID(UUID.fromString("55b1bba8-eb9c-4786-b79c-ef0e8d6c2d1a"));

        user2 = new User("user@example.com", "Regular User", Role.ROLE_USER, "password", Timestamp.from(Instant.now()));
        user2.setUserID(UUID.fromString("53f187b8-575c-4483-9fac-81f433600eee"));
    }

    @Test
    public void shouldFindAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        List<UserDetailsResponse> userDetailsResponses = userService.findAll();

        assertThat(userDetailsResponses)
                .extracting("userID", "name", "email", "role", "createdAt")
                .contains(tuple(user1.getUserID(), user1.getName(), user1.getEmail(), user1.getRole(), user1.getCreatedAt()),
                        tuple(user2.getUserID(), user2.getName(), user2.getEmail(), user2.getRole(), user2.getCreatedAt()));
    }

    @Test
    public void shouldDeleteUser() {
        when(userRepository.findById(user1.getUserID())).thenReturn(Optional.of(user1));

        ResponseEntity<Object> responseEntity = userService.deleteUser(user1.getUserID());

        verify(userRepository).delete(any(User.class));
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void shouldDeleteUserInvalidUser() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(AccountNotExistsException.class, () -> userService.deleteUser(UUID.randomUUID()));
    }

    @Test
    public void shouldUpdateSettings() {
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getUserID())).thenReturn(Optional.of(user2));
        when(userRepository.existsUserByEmail("test@pixpayview.com")).thenReturn(false);

        ResponseEntity<Object> responseEntity = userService.updateSettings(user1.getEmail(), user2.getUserID(), new UserSettingsRequest());

        verify(userDetailsService).updateUserDetails(any(User.class));
        verify(userRepository).save(any(User.class));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void shouldUpdateSettingsPassword() {
        UserSettingsRequest userSettingsRequest = new UserSettingsRequest();
        userSettingsRequest.setPassword("newPassword");

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user1.getUserID())).thenReturn(Optional.of(user1));
        when(userRepository.existsUserByEmail("test@pixpayview.com")).thenReturn(false);
        when(passwordEncoder.encode(userSettingsRequest.getPassword())).thenReturn("encodedNewPassword");

        ResponseEntity<Object> responseEntity = userService.updateSettings(user1.getEmail(), user1.getUserID(), userSettingsRequest);

        verify(passwordEncoder).encode(userSettingsRequest.getPassword());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void shouldUpdateSettingsUserNotFound() {
        when(userRepository.findById(user1.getUserID())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user2.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = userService.updateSettings(user2.getEmail(), user1.getUserID(), new UserSettingsRequest());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void shouldUpdateSettingsNotPermission() {
        when(userRepository.findById(user1.getUserID())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(user2.getEmail())).thenReturn(Optional.of(user2));

        assertThrows(ForbiddenException.class, () -> userService.updateSettings(user2.getEmail(), user1.getUserID(), new UserSettingsRequest()));
    }

    @Test
    public void shouldUpdateSettingsEmailAlreadyExists() {
        when(userRepository.findById(user1.getUserID())).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        when(userRepository.existsUserByEmail(user1.getEmail())).thenReturn(true);

        UserSettingsRequest userSettingsRequest = new UserSettingsRequest();
        userSettingsRequest.setEmail(user1.getEmail());

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateSettings(user1.getEmail(), user1.getUserID(), userSettingsRequest));
    }

}