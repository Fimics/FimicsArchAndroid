package org.devio.as.hi.hiconcurrent_demo.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示  多个线程 去打印纸张,每个线程 打印张(ReentrantLock 公平锁，非公平锁)
 * <p>
 * 公平锁：交易
 * 非公平锁：synchorinzed，场景比比皆是
 */
public class ReentrantLockDemo2 {

    static class ReentrantLockTask {

        ReentrantLock lock = new ReentrantLock(false
        );

        void print() {
            String name = Thread.currentThread().getName();
            try {
                lock.lock();
                //打印两次
                System.out.println(name + "第一次打印");
                Thread.sleep(1000);
                lock.unlock();

                lock.lock();
                System.out.println(name + "第二次打印");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }

    public static void main(String[] args) {

        final ReentrantLockTask task = new ReentrantLockTask();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                task.print();
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
    }

}
