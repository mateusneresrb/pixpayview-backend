package dev.mateusneres.pixpayviewbackend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    private String password;

}
