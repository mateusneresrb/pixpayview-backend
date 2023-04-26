package dev.mateusneres.pixpayview.exceptions;

import dev.mateusneres.pixpayview.dtos.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<Object> handleException(CustomException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorMessage(), ex.getErrorCode());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", errorResponse);

        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatusCode()));
    }
}