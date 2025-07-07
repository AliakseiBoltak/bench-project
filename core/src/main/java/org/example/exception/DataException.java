package org.example.exception;

public class DataException extends RuntimeException {
    public DataException(String message) {
        super(message);
    }

    public DataException(Throwable cause) {
        super(cause);
    }
}