package dev.mateusneres.pixpayview.exceptions;

public abstract class CustomException extends RuntimeException{

    public abstract int getStatusCode();

    public abstract int getErrorCode();

    public abstract String getErrorMessage();

}