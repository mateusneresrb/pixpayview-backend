package dev.mateusneres.pixpayview.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class BadRequestException extends CustomException {

    private final int errorCode;
    private final String errorMessage;

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

}