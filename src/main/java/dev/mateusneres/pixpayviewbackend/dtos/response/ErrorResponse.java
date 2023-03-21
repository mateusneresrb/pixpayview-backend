package dev.mateusneres.pixpayviewbackend.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private String message;
    private int code;

}
