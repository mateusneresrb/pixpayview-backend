package dev.mateusneres.pixpayview.dtos.request;

import dev.mateusneres.pixpayview.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequest {

    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

}
