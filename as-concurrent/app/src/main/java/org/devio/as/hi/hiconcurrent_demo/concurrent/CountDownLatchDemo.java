package org.devio.as.hi.hiconcurrent_demo.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 演示一个 多人过山车的场景.
 * <p>
 * 我们假设有5人 去乘坐做过山车，----等待5人全部准备好，才能发车
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch downLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(new Random().nextInt(4000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "准备好了");
                    downLatch.countDown();
                }
            }).start();
        }

        downLatch.await();
        System.out.println("所有人都准备好了，准备发车...");
    }
}
