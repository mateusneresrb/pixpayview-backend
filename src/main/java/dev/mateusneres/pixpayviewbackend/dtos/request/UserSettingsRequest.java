package dev.mateusneres.pixpayviewbackend.dtos.request;

import lombok.Data;

@Data
public class UserSettingsRequest {

    private String email;
    private String password;
    private String name;

}
