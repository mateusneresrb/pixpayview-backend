package dev.mateusneres.pixpayview.dtos.response;

import dev.mateusneres.pixpayview.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Setter
@Getter
public class UserDetailsResponse {

    private UUID userID;
    private String name;
    private String email;
    private Role role;
    private Timestamp updatedAt;
    private Timestamp createdAt;

}
