package dev.mateusneres.pixpayview.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.mateusneres.pixpayview.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSettingsRequest {

    @Email(message = "Email should be valid")
    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;

}
