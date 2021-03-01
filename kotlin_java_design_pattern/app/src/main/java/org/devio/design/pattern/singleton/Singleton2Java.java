package org.devio.design.pattern.singleton;

/**
 * 懒汉式-Java实现
 */
public class Singleton2Java {
    private static Singleton2Java instance;

    private Singleton2Java() {
    }

    public static synchronized Singleton2Java getInstance() {
        if (instance == null) {
            instance = new Singleton2Java();
        }
        return instance;
    }
}
