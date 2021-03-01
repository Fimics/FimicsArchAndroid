package org.devio.design.pattern.singleton;

/**
 * 静态内部类式单例-Java实现
 */
public class Singleton4Java {
    private static class SingletonProvider {
        private static Singleton4Java instance = new Singleton4Java();
    }

    private Singleton4Java() {
    }

    public static Singleton4Java getInstance() {
        return SingletonProvider.instance;
    }
}

