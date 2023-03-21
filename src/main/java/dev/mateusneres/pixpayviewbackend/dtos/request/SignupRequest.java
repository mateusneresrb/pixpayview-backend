package dev.mateusneres.pixpayviewbackend.dtos.request;

import dev.mateusneres.pixpayviewbackend.enums.Role;
import lombok.Data;

@Data
public class SignupRequest {

    private String email;
    private String password;
    private String name;
    private Role role;

}
