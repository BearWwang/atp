package com.snake.drivers.util;


/**
 * 观察者接口，如果实现了该接口可以对被观察进行观察，当被观察者发出消息，观察者可以接受该消息
 */
public interface Listener {
    void update(Listenable o, Object arg);
}
