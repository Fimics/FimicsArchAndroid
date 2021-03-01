package org.devio.design.pattern.singleton;

/**
 * 双重校验锁-Java实现
 */
public class Singleton3Java {
    private volatile static Singleton3Java instance;

    private Singleton3Java() {
    }

    public static Singleton3Java getInstance() {
        if (instance == null) {
            synchronized (Singleton3Java.class) {
                if (instance == null) {
                    instance = new Singleton3Java();
                }
            }
        }
        return instance;
    }
}
