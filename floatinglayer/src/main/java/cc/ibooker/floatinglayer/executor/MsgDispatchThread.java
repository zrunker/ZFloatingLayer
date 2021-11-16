package cc.ibooker.floatinglayer.executor;

import android.os.Message;
import android.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import cc.ibooker.floatinglayer.util.Config;

/**
 * @program: ZFloatingLayer
 * @description: 延迟Message消息分发线程
 * @author: zoufengli01
 * @create: 2021-11-11 11:13
 **/
public abstract class MsgDispatchThread implements Runnable {
    /*执行线程*/
    private Thread thread;
    /*消息队列*/
    private final Queue<Message> queue;
    /*线程控制开关*/
    private boolean isCanRun = true;
    /*锁，延迟执行*/
    private final Object lock = new Object();

    public MsgDispatchThread() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.thread = new Thread(this);
        this.start();
    }

    @Override
    public void run() {
        synchronized (lock) {
            while (isCanRun) {
                try {
                    /*设置延迟*/
                    lock.wait(Config.msgDelayTimeDiff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("MsgDispatchThread", "error: " + e.getMessage());
                }
                if (queue.size() > 0) {
                    dispatchMessage(queue.poll());
                }
            }
        }
    }

    /**
     * 添加消息
     *
     * @param data 消息
     */
    public <T> void addMsg(T data) {
        if (data != null) {
            Message message = Message.obtain();
            message.obj = data;
            queue.add(message);
        }
    }

    /**
     * 关闭线程
     */
    public void stop() {
        this.isCanRun = false;
    }

    /**
     * 开启线程
     */
    public void start() {
        try {
            this.isCanRun = true;
            if (!this.thread.isAlive()) {
                this.thread = new Thread(this);
                this.thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MsgDispatchThread", "error: " + e.getMessage());
        }
    }

    public abstract void dispatchMessage(Message msg);
}
