package ru.igrey.dev.exception;

public class DateTimeFormatException extends RuntimeException{
    public DateTimeFormatException(String message) {
        super(message);
    }

    public DateTimeFormatException() {
        super();
    }

    public DateTimeFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateTimeFormatException(Throwable cause) {
        super(cause);
    }

    protected DateTimeFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
