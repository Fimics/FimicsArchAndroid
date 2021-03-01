package org.devio.as.hi.hiconcurrent_demo.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;

public class CountdownLatchDemo {
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);
        System.out.println("主线程开始执行……");
        //第一个子线程执行
        new Thread(createRunnable(latch)).start();

        new Thread(createRunnable(latch)).start();

        System.out.println("等待两个线程执行完毕……");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("两个子线程都执行完毕，继续执行主线程");
    }

    @NotNull
    private static Runnable createRunnable(final CountDownLatch latch) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    System.out.println("子线程：" + Thread.currentThread().getName() + "执行完成");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        };
    }
}
