package org.devio.as.hi.hiconcurrent_demo.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 一个用原子类修饰，一个用volatile修饰，在多线程的情况做自增，然后输出最后得值
 */
public class AtomicDemo {


    public static void main(String[] args) throws InterruptedException {
        final AtomicTask task = new AtomicTask();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    task.incrementVolatile();
                    task.incrementAtomic();
                }
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("原子类的结果：" + task.atomicInteger.get());
        System.out.println("volatile修饰的结果：" + task.volatileCount);
    }

    static class AtomicTask {
        AtomicInteger atomicInteger = new AtomicInteger();
        volatile int volatileCount = 0;

        void incrementAtomic() {
            atomicInteger.getAndIncrement();
        }

        void incrementVolatile() {
            volatileCount++;
            //volatileCount = volatileCount + 1;
            //volatileCount = 10000;
        }
    }
}
