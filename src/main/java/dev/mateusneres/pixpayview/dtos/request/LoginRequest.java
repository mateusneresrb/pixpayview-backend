package dev.mateusneres.pixpayview.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    private String password;

}
