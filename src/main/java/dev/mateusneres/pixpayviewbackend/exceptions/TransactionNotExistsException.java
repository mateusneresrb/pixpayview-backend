package dev.mateusneres.pixpayviewbackend.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class TransactionNotExistsException extends CustomException {

    private final int errorCode;
    private final String errorMessage;

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }

}
