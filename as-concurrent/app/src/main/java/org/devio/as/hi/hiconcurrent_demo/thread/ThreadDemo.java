package org.devio.as.hi.hiconcurrent_demo.thread;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class ThreadDemo {
    private static final String TAG = "ConcurrentTest";
    private static final int MSG_WHAT_1 = 1;

    public static void test() {
        //asyncTask的几种使用方式
        testAsynctask();

        //HandlerThread的使用方式
        testHanderThread();

        //wait-notify线程同步，流程控制
        testWaitNotify();

        //thread.join线程同步，流程控制
        testThreadJoin();

        //thread.sleep线程同步,流程控制
        testThreadSleep();

        //main-thread 主-子线程通信
        testThreadCommunication();
    }

    private static void testThreadCommunication() {
        //主线程向子线程发消息
        class LooperThread extends Thread {
            private Looper looper;

            public LooperThread(String name) {
                super(name);
            }

            public Looper getLooper() {
                synchronized (this) {
                    if (looper == null && isAlive()) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return looper;
            }

            @Override
            public void run() {
                Looper.prepare();
                synchronized (this) {
                    looper = Looper.myLooper();
                    notify();
                }
                Looper.loop();
            }
        }


        LooperThread looperThread = new LooperThread("looper-thread");
        looperThread.start();
        Handler handler = new Handler(looperThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.e(TAG, "handleMessage: " + msg.what);
                Log.e(TAG, "handleMessage: " + Thread.currentThread().getName());
            }
        };
        handler.sendEmptyMessage(MSG_WHAT_1);
    }

    private static void testThreadSleep() {
        //适用于线程暂时休眠，让出CPU使用权，也可用于多线程的同步
        final Object object = new Object();
        class Runnable1 implements Runnable {
            @Override
            public void run() {
                synchronized (object) {
                    Log.e(TAG, "run: thread1 start");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "run: thread1 end");
                }

            }
        }


        class Runnable2 implements Runnable {
            @Override
            public void run() {
                synchronized (object) {
                    Log.e(TAG, "run: thread2 start");
                    Log.e(TAG, "run: thread2 end");
                }
            }
        }
        Thread thread1 = new Thread(new Runnable1());
        Thread thread2 = new Thread(new Runnable2());
        thread1.start();
        thread2.start();
    }

    private static void testThreadJoin() {
        //一个线程需要等待另一个线程执行完才能继续的的场景
        //join向当前线程插入一条任务
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: 1: " + System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "run: 2: " + System.currentTimeMillis());
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "test: 3: " + System.currentTimeMillis());
    }

    private static volatile boolean hasNotify = false;

    private static void testWaitNotify() {
        //适用于多线程同步,一个线程需要等待另一个线程的执行结果，或者部分结果
        //wait-notify 调用顺序
        final Object object = new Object();
        class Runnable1 implements Runnable {
            @Override
            public void run() {
                synchronized (object) {
                    Log.e(TAG, "run: thread1 start");
                    try {
                        if (!hasNotify) {
                            object.wait(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "run: thread1 end");
                }
            }
        }


        class Runnable2 implements Runnable {
            @Override
            public void run() {
                synchronized (object) {
                    Log.e(TAG, "run: thread2 start");
                    object.notify();
                    hasNotify = true;
                    Log.e(TAG, "run: thread2 end");
                }
            }
        }

        Thread thread1 = new Thread(new Runnable1());
        Thread thread2 = new Thread(new Runnable2());
        thread1.start();
        thread2.start();
    }

    // 适用于主线程需要和子线程通信的场景，
    // 应用于持续性任务，比如轮训，
    private static void testHanderThread() {
        class MyHandler extends Handler {
            public MyHandler(Looper looper) {
                super(looper);
            }

            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Log.e(TAG, "handleMessage: " + msg.what);
                Log.e(TAG, "handleMessage: " + Thread.currentThread().getName());
            }
        }

        HandlerThread handlerThread = new HandlerThread("handler-thread");
        handlerThread.start();

        MyHandler myHandler = new MyHandler(handlerThread.getLooper());
        myHandler.sendEmptyMessage(MSG_WHAT_1);
    }

    private static void testAsynctask() {
        class MyAsyncTask extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... params) {
                for (int i = 0; i < 10; i++) {
                    publishProgress(i * 10);
                }
                return params[0];
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                Log.e(TAG, "onProgressUpdate: " + values[0]);
            }

            @Override
            protected void onPostExecute(String result) {
                Log.e(TAG, "onPostExecute: " + result);
            }
        }

        //适用于需要知道任务执行进度并更新UI的场景
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("execute myasynctask");

        //以这种方式提交的任务，所有任务串行执行，即先来后到，但是如果其中有一条任务休眠了，或者执行时间过长，后面的任务都将被阻塞
        //是应用串行任务执行
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: AsyncTask execute");
            }
        });

        //适用于并发任务执行
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: THREAD_POOL_EXECUTOR AsyncTask execute");
            }
        });

        for (int i = 0; i < 10; i++) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + System.currentTimeMillis());
                }
            });
        }
    }
}
