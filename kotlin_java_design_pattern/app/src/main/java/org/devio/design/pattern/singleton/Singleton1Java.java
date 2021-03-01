package org.devio.design.pattern.singleton;

/**
 * 饿汉式-Java实现
 */
public class Singleton1Java {
    private static Singleton1Java instance = new Singleton1Java();

    public static Singleton1Java getInstance() {
        return instance;
    }
}
