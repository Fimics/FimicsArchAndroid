package org.devio.as.hi.hiconcurrent_demo.concurrent;


import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 演示 多人故宫游玩，但是同一时刻限流3人
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(3, true);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String name = Thread.currentThread().getName();
                        semaphore.acquire(2);

                        System.out.println(name + "获取到了许可证，进去游玩了");

                        Thread.sleep(new Random().nextInt(5000));

                        semaphore.release(2);

                        System.out.println(name + "归还了许可证");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
