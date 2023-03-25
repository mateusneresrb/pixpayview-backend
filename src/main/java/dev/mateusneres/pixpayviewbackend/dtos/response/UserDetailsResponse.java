package dev.mateusneres.pixpayviewbackend.dtos.response;

import dev.mateusneres.pixpayviewbackend.enums.Role;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class UserDetailsResponse {

    private UUID userID;
    private String name;
    private String email;
    private Role role;
    private Timestamp updatedAt;
    private Timestamp createdAt;

}
