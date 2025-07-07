package org.example.exception;

public class DataException extends RuntimeException {

    public DataException(Throwable cause) {
        super(cause);
    }

    public DataException(String s) {
        super(s);
    }
}