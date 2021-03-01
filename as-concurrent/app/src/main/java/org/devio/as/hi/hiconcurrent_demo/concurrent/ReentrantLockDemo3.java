package org.devio.as.hi.hiconcurrent_demo.concurrent;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 我们演示 生产者与消费者的场景，利用的是ReentrantLock condition 条件对象，能够指定唤醒某个线程去工作
 * <p>
 * <p>
 * 生产者是：一个boss  去生产砖，砖的序列号为偶数，那么工人2去搬，奇数号让工人去去搬
 * <p>
 * 消费者是两个工人，有砖搬就搬，没转搬就休息
 */
public class ReentrantLockDemo3 {

    static class ReentrantLockTask {

        private Condition worker1Condition, worker2Condition;
        ReentrantLock lock = new ReentrantLock(true);

        volatile int flag = 0;//砖的序列号

        public ReentrantLockTask() {
            worker1Condition = lock.newCondition();
            worker2Condition = lock.newCondition();
        }

        //工人1搬砖
        void work1() {
            try {
                lock.lock();
                if (flag == 0 || flag % 2 == 0) {
                    System.out.println("worker1 无砖可搬,休息会");
                    worker1Condition.await();
                }

                System.out.println("worker1 搬到的砖是：" + flag);
                flag = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }


        //工人2搬砖
        void work2() {
            try {
                lock.lock();
                if (flag == 0 || flag % 2 != 0) {
                    System.out.println("worker2 无砖可搬,休息会");
                    worker2Condition.await();
                }

                System.out.println("worker2 搬到的砖是：" + flag);
                flag = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        void boss() {

            try {
                lock.lock();
                flag = new Random().nextInt(100);
                if (flag % 2 == 0) {
                    worker2Condition.signal();
                    System.out.println("生产出来了砖，唤醒工人2去搬：" + flag);
                } else {
                    worker1Condition.signal();
                    System.out.println("生产出来了砖，唤醒工人1去搬：" + flag);
                }
            } finally {
                lock.unlock();
            }
        }


        public static void main(String[] args) {
            final ReentrantLockTask lockTask = new ReentrantLockTask();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        lockTask.work1();
                    }
                }
            }).start();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        lockTask.work2();
                    }
                }
            }).start();


            for (int i = 0; i < 10; i++) {
                lockTask.boss();
            }
        }
    }
}
