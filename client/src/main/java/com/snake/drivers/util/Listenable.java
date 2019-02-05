package com.snake.drivers.util;

import java.util.Vector;

/**
 * 被观察者类,使用时需要注册观察者，发送消息是使用notifyObservers方法
 */
public class Listenable {

    private boolean changed = false;
    private Vector<Listener> obs;

    public Listenable() {
        obs = new Vector<>();
    }

    /**
     * 添加观察者
     * @param o 继承观察者对象的类
     */
    public synchronized void addListener(Listener o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    /**
     * 删除观察者
     * @param o 继承观察者对象的类
     */
    public synchronized void deleteListener(Listener o) {
        obs.removeElement(o);
    }

    /**
     * 通知观察着，不带参数
     */
    public void notifyListener() {
        notifyListener(null);
    }

    /**
     * 通知观察着，带参数
     * @param arg 通知的信息
     */
    public void notifyListener(Object arg) {
        Object[] arrLocal;

        synchronized (this) {
            // 如果没有发生改变则不通知观察者
            if (!changed)
                return;
            // 将观察者对象列表数组形式
            arrLocal = obs.toArray();
            // 重置通知
            clearChanged();
        }
        // 执行所有观察者的update方法
        for (int i = arrLocal.length-1; i>=0; i--)
            ((Listener)arrLocal[i]).update(this, arg);
    }

    /**
     * 删除观察者
     */
    public synchronized void deleteAllListener() {
        obs.removeAllElements();
    }

    /**
     * 发生改变时需要设置该标志位
     */
    public synchronized void setChanged() {
        changed = true;
    }

    /**
     * 重置标志位
     */
    public synchronized void clearChanged() {
        changed = false;
    }

    /**
     * 判断是否发生改变
     * @return 返回标志位
     */
    public synchronized boolean hasChanged() {
        return changed;
    }

    /**
     * 返回有多少个观察者
     * @return 观察者个数
     */
    public synchronized int countListener() {
        return obs.size();
    }



}
