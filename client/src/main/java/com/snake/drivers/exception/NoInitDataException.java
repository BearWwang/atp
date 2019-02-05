package com.snake.drivers.exception;

public class NoInitDataException extends RuntimeException {
    public NoInitDataException() {
        super();
    }

    public NoInitDataException(String message) {
        super(message);
    }
}
