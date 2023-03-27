package dev.mateusneres.pixpayviewbackend.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.mateusneres.pixpayviewbackend.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSettingsRequest {

    @Email(message = "Email should be valid")
    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;

}
