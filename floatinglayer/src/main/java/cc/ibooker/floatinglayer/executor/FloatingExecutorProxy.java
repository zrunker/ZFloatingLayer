package cc.ibooker.floatinglayer.executor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

import cc.ibooker.floatinglayer.event.FloatingEvent;
import cc.ibooker.floatinglayer.flayer.IFloatingLayer;
import cc.ibooker.floatinglayer.observer.ISurface;
import cc.ibooker.floatinglayer.observer.SurfaceObserver;
import cc.ibooker.floatinglayer.vholder.IViewHolder;

/**
 * @program: ZFloatingLayer
 * @description: 浮层对外执行代理类
 * @author: zoufengli01
 * @create: 2021-11-11 20:01
 **/
public class FloatingExecutorProxy implements IExecutor {
    private final FloatingExecutor fExecutor;
    /*切换主线程Handler*/
    private final ZHandler zHandler;
    /*消息执行线程*/
    private final MsgDispatchThread mDThread;

    /*自定义Handler*/
    private static class ZHandler extends Handler {
        private final WeakReference<FloatingExecutorProxy> mWeakR;

        ZHandler(Looper looper, FloatingExecutorProxy proxy) {
            super(looper);
            this.mWeakR = new WeakReference<>(proxy);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            FloatingExecutorProxy proxy = mWeakR.get();
            if (proxy != null) {
                if (msg.obj instanceof FloatingEvent) {
                    FloatingEvent<?> fEvent = (FloatingEvent<?>) msg.obj;
                    proxy.fExecutor.execute(fEvent);
                }
            }
        }
    }

    public FloatingExecutorProxy(Context context) {
        this.fExecutor = new FloatingExecutor(context);
        this.zHandler = new ZHandler(Looper.myLooper(), this);
        // 开启常驻线程
        this.mDThread = new MsgDispatchThread() {
            @Override
            public void dispatchMessage(Message msg) {
                zHandler.sendMessage(msg);
            }
        };
        // 注册监听
        SurfaceObserver.getInstance().registerListener(new ISurface() {
            @Override
            public void surfaceCreated() {
                mDThread.start();
            }

            @Override
            public void surfaceDestroyed() {
                mDThread.stop();
            }
        });
    }

    @Override
    public <T> boolean execute(FloatingEvent<T> fEvent) {
        if (fEvent != null) {
            mDThread.addMsg(fEvent);
        }
        return true;
    }

    @Override
    public IFloatingLayer fLayer() {
        return fExecutor.fLayer();
    }

    @Override
    public <T> void preViewHolder(@NonNull IViewHolder<T> vHolder, int num) {
        fExecutor.preViewHolder(vHolder, num);
    }

    @Override
    public void onShow() {
        fExecutor.onShow();
    }

    @Override
    public void onHide() {
        fExecutor.onHide();
    }

    @Override
    public void onDestroy() {
        this.mDThread.stop();
        this.fExecutor.onDestroy();
    }

    @Override
    public void onClear() {
        fExecutor.onClear();
    }
}
