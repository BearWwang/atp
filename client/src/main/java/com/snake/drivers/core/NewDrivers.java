package com.snake.drivers.core;

public class NewDrivers implements NewDriver {
    @Override
    public Driver getInstance(Class clazz) {
        return new Driver(clazz);
    }
}
