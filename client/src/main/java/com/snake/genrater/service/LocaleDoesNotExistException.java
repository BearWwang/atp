package com.snake.genrater.service;

public class LocaleDoesNotExistException extends RuntimeException {
    public LocaleDoesNotExistException(String message) {
        super(message);
    }
}
