package dev.mateusneres.pixpayview.dtos.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class LoginResponse {

    private final String accessToken;

}
