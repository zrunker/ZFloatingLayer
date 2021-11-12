package cc.ibooker.floatinglayer.core.executor;


import static cc.ibooker.floatinglayer.core.util.ConstantUtil.IS_OPEN_EXECUTE_DELAYED;
import static cc.ibooker.floatinglayer.core.util.ConstantUtil.MAX_CONCURRENT_NUM;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import cc.ibooker.floatinglayer.core.anim.AnimService;
import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.event.FloatingEvent;
import cc.ibooker.floatinglayer.core.flayer.IFloatingLayer;
import cc.ibooker.floatinglayer.core.vholder.IViewHolder;

/**
 * @program: ZFloatingLayer
 * @description: 浮层对外执行代理类
 * @author: zoufengli01
 * @create: 2021-10-28 15:22
 **/
public class FloatingExecutorProxy implements IExecutor {
    private final FloatingExecutor fExecutor;
    private Queue<FloatingEvent<?>> queue;
    private ZHandler zHandler;

    /**
     * 定义Handler执行延迟事件
     */
    private static class ZHandler extends Handler {
        private final WeakReference<FloatingExecutorProxy> mWeakRef;

        public ZHandler(FloatingExecutorProxy proxy) {
            this.mWeakRef = new WeakReference<>(proxy);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            FloatingExecutorProxy proxy = mWeakRef.get();
            if (proxy != null) {
                if (msg.obj != null) {
                    FloatingEvent<?> fEvent = (FloatingEvent<?>) msg.obj;
                    proxy.fExecutor.execute(fEvent);
                }
                if (proxy.getReadyEventNum() <= MAX_CONCURRENT_NUM) {
                    proxy.executeQueueDelayed(true);
                }
            }
        }
    }

    /**
     * 构造方法
     */
    public FloatingExecutorProxy(Context context) {
        this.queue = new LinkedBlockingQueue<>();
        this.fExecutor = new FloatingExecutor(context);
        this.fExecutor.setAnimService(new AnimService() {
            @Override
            protected void animInitBack(@NonNull ViewFloating vFloating,
                                        @NonNull FloatingEvent<?> fEvent) {
                fExecutor.fLayer().resetChildViewLocation(vFloating.view,
                        fEvent.combinationEnum.getLocationStrategy());
            }

            @Override
            protected void animStartBack(@NonNull ViewFloating vFloating,
                                         @NonNull FloatingEvent<?> fEvent) {

            }

            @Override
            protected void animEndBack(@NonNull ViewFloating viewFloating,
                                       @NonNull FloatingEvent<?> fEvent) {
                if (queue != null && queue.size() > 0) {
                    // 移除事件
                    queue.remove(fEvent);
                    // 执行队列数据
                    if (IS_OPEN_EXECUTE_DELAYED) {
                        executeQueueDelayed(false);
                    } else {
                        fExecutor.execute(getFloatingEvent());
                    }
                }
            }
        });
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
        if (zHandler != null) {
            zHandler.removeCallbacksAndMessages(null);
            zHandler = null;
        }
        if (queue != null) {
            queue.clear();
            queue = null;
        }
        fExecutor.onDestroy();
    }

    @Override
    public void onClear() {
        fExecutor.onClear();
    }

    @Override
    public <T> boolean execute(FloatingEvent<T> fEvent) {
        if (fEvent != null) {
            if (IS_OPEN_EXECUTE_DELAYED) {
                queue.add(fEvent);
                executeQueueDelayed(false);
            } else {
                if (MAX_CONCURRENT_NUM <= 0) {
                    fExecutor.execute(fEvent);
                } else {
                    queue.add(fEvent);
                    if (getReadyEventNum() <= MAX_CONCURRENT_NUM) {
                        fExecutor.execute(fEvent);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 执行延迟事件
     *
     * @param isNow 是否马上执行
     */
    private void executeQueueDelayed(boolean isNow) {
        if (isNow || getReadyEventNum() <= 0) {
            executeQueueDelayed();
        }
    }

    /**
     * 执行延迟事件（延迟300ms）
     */
    private void executeQueueDelayed() {
        if (queue != null && queue.size() > 0) {
            // 获取有效事件
            FloatingEvent<?> uEvent = getFloatingEvent();
            // 执行获取到的事件
            if (uEvent != null) {
                Message message = Message.obtain();
                message.obj = uEvent;
                if (zHandler == null) {
                    zHandler = new ZHandler(this);
                }
                zHandler.sendMessageDelayed(message, 600);
            }
        }
    }

    /**
     * 获取有效事件
     */
    private FloatingEvent<?> getFloatingEvent() {
        FloatingEvent<?> uEvent = null;
        for (FloatingEvent<?> fEvent : queue) {
            if (!fEvent.isReady) {
                uEvent = fEvent;
                uEvent.isReady = true;
                break;
            }
        }
        return uEvent;
    }

    /**
     * 获取正在运行事件数量
     */
    private int getReadyEventNum() {
        int num = 0;
        for (FloatingEvent<?> fEvent : queue) {
            if (fEvent.isReady) {
                num++;
            }
        }
        return num;
    }
}
