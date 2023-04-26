package dev.mateusneres.pixpayview.dtos.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final String message;
    private final int code;

}
