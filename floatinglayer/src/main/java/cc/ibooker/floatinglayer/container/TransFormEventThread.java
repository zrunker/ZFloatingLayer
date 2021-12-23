package cc.ibooker.floatinglayer.container;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cc.ibooker.floatinglayer.cache.ViewFloating;
import cc.ibooker.floatinglayer.event.AnimFunStyle;
import cc.ibooker.floatinglayer.executor.FloatingMsg;
import cc.ibooker.floatinglayer.flayer.FLayerConfig;
import cc.ibooker.floatinglayer.location.LStrategyFactory;
import cc.ibooker.floatinglayer.location.strategy.AbsStrategy;
import cc.ibooker.floatinglayer.observer.IViewRefreshObserver;
import cc.ibooker.floatinglayer.observer.ViewRefreshObserver;
import cc.ibooker.floatinglayer.util.Config;
import cc.ibooker.floatinglayer.vholder.ViewState;


/**
 * @program: ZFloatingLayer
 * @description: 事件消耗线程
 * @author: zoufengli01
 * @create: 2021-11-11 12:30
 **/
public class TransFormEventThread implements IViewRefreshObserver {
    /*浮层配置信息*/
    private final FLayerConfig fLayerConfig;
    /*处理Event子线程，用于计算View显示位置*/
    private HandlerThread tTread;
    /*Event子线程具体实施Handler*/
    private THandler tHandler;
    /*绘制时间间隔 ms*/
    private final long timeDiff;
    /*浮层消息缓存集合*/
    private final Set<FloatingBean> eventSet;
    /*锁，处理Iterator*/
    private final Object lock = new Object();

    @Override
    public void onRefresh(View view) {
        if (view != null) {
            synchronized (lock) {
                filter();
                // 更新bitmap
                for (FloatingBean item : eventSet) {
                    View targetView = item.vFloating.view;
                    if (targetView == view) {
                        item.bitmap = viewToBitmap(view);
                        return;
                    }
                }
            }
        }
    }

    /*自定义Handler*/
    private static class THandler extends Handler {
        WeakReference<TransFormEventThread> mWeakR;

        THandler(@NonNull Looper looper, TransFormEventThread parent) {
            super(looper);
            this.mWeakR = new WeakReference<>(parent);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            TransFormEventThread parent = mWeakR.get();
            if (parent != null) {
                Object obj = msg.obj;
                if (obj instanceof FloatingMsg) {
                    FloatingMsg fMsg = (FloatingMsg) obj;

                    FloatingBean fBean = new FloatingBean();
                    fBean.vFloating = fMsg.vFloating;
                    fBean.animDuration = fMsg.animDuration + Config.animDurationIncrement;

                    View view = fBean.vFloating.view;
                    fBean.bitmap = parent.viewToBitmap(view);

                    // 获取父控件属性
                    FLayerConfig fLayerConfig = parent.fLayerConfig;

                    // 设置fEvent参数
                    fBean.height = view.getMeasuredHeight();
                    fBean.width = view.getMeasuredWidth();

                    switch (fMsg.animFunStyle) {
                        case AnimFunStyle.RIGHT_TO_LEFT:
                            // 从右向左
                            fBean.fromX = fLayerConfig.startX + fLayerConfig.width + fLayerConfig.childHorMargin;
                            fBean.toX = fLayerConfig.startX - fBean.width - fLayerConfig.childHorMargin;
                            fBean.disDiff = (fBean.fromX - fBean.toX) / fBean.animDuration * parent.timeDiff;

                            AbsStrategy rtlStrategy = LStrategyFactory
                                    .getStrategy(parent.eventSet, fMsg.locationStrategy);
                            if (rtlStrategy != null) {
                                int[] margins = rtlStrategy.getMargins(fBean, fLayerConfig);
                                fBean.topY = margins[1];
                            }
                            break;
                        case AnimFunStyle.SHOW_TO_HIDE:
                            // 淡入淡出
                            AbsStrategy sthStrategy = LStrategyFactory
                                    .getStrategy(parent.eventSet, fMsg.locationStrategy);
                            if (sthStrategy != null) {
                                int[] margins = sthStrategy.getMargins(fBean, fLayerConfig);
                                fBean.fromX = margins[0];
                                fBean.topY = margins[1];
                            }
                            break;
                    }

                    // 添加到集合
                    parent.addSet(fBean);
                }
            }
        }
    }

    public TransFormEventThread(@NonNull FLayerConfig fLayerConfig,
                                long timeDiff) {
        ViewRefreshObserver.getInstance().registerListener(this);
        this.fLayerConfig = fLayerConfig;
        this.timeDiff = timeDiff;
        this.eventSet = Collections.synchronizedSet(new HashSet<>());
        this.initHTread();
    }

    /**
     * 构建HandlerThread
     */
    private void initHTread() {
        if (tTread == null || !tTread.isAlive()) {
            tTread = new HandlerThread("TransFormEventThread");
            tTread.start();
        }
        if (tHandler == null) {
            tHandler = new THandler(tTread.getLooper(), this);
        }
    }

    /**
     * View转Bitmap
     *
     * @param view 目标View
     */
    private Bitmap viewToBitmap(@NonNull View view) {
        try {
            // 设置测量模式
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            view.layout(0, 0, measuredWidth, measuredHeight);

            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(bitmap);
//            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            view.draw(canvas);

            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    // 过滤过期事件
    private void filter() {
        synchronized (lock) {
            Iterator<FloatingBean> iterator = eventSet.iterator();
            while (iterator.hasNext()) {
                FloatingBean item = iterator.next();
                if (item == null
                        || item.fromX <= item.toX
                        || item.showTime >= item.animDuration
                        || item.bitmap == null) {
                    iterator.remove();
                    if (item != null) {
                        if (item.vFloating != null) {
                            item.vFloating.updateState(ViewState.END);
                        }
                        if (item.bitmap != null) {
                            item.bitmap = null;
                        }
                    }
                }
            }
        }
    }

    /**
     * 添加事件到Set集合
     *
     * @param fEvent 待处理事件数据
     */
    private void addSet(FloatingBean fEvent) {
        if (fEvent != null) {
            synchronized (lock) {
                filter();
                // 修改View状态
                fEvent.vFloating.updateState(ViewState.START);
                // 添加到集合
                eventSet.add(fEvent);
            }
        }
    }

    /**
     * 获取事件集合
     */
    public Set<FloatingBean> getEventSet() {
        synchronized (lock) {
            return new HashSet<>(eventSet);
        }
    }

    /**
     * 移除特定事件
     */
    public void remove(ViewFloating vFloating) {
        if (vFloating != null) {
            synchronized (lock) {
                Iterator<FloatingBean> iterator = eventSet.iterator();
                while (iterator.hasNext()) {
                    FloatingBean fEvent = iterator.next();
                    if (fEvent.vFloating == vFloating) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 转化消息
     *
     * @param msg 待处理消息
     */
    public void transformMsg(Message msg) {
        if (msg != null) {
            initHTread();
            this.tHandler.sendMessage(msg);
        }
    }

    /**
     * 清空数据
     */
    public void clear() {
        synchronized (lock) {
            eventSet.clear();
        }
    }
}
