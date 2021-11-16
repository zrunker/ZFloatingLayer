package cc.ibooker.floatinglayer.container;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.Set;

import cc.ibooker.floatinglayer.executor.FloatingMsg;
import cc.ibooker.floatinglayer.flayer.FLayerConfig;
import cc.ibooker.floatinglayer.observer.SurfaceObserver;
import cc.ibooker.floatinglayer.vholder.ViewState;

/**
 * @program: ZFloatingLayer
 * @description: 浮层容器，由三个线程实现
 * @author: zoufengli01
 * @create: 2021-11-10 15:58
 **/
public abstract class SurfaceContainer extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {
    /*SurfaceHolder实例*/
    private SurfaceHolder sHolder;
    /*标记是否能够绘制*/
    private boolean isCanDraw;
    /*Surface画布*/
    private Canvas mCanvas;

    /*记录上一次绘制时间 ms*/
    private long preDrawTime;

    /*转化Event子线程，用于计算View显示位置*/
    private TransFormEventThread mTransFormEventThread;

    public SurfaceContainer(Context context) {
        this(context, null);
    }

    public SurfaceContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setZOrderOnTop(true);
        // 获取SurfaceHolder
        sHolder = getHolder();
        sHolder.setFormat(PixelFormat.TRANSPARENT);
        sHolder.addCallback(this);
        // 初始化处理Event子线程
        mTransFormEventThread = new TransFormEventThread(getFLayerConfig(), SurfaceConfig.DRAW_TIME_DIFF);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        isCanDraw = true;
        // 开启常驻子线程
        new Thread(this).start();
        // 开启消息分发
        SurfaceObserver.getInstance().surfaceCreated();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder,
                               int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isCanDraw = false;
        // 停止消息分发
        SurfaceObserver.getInstance().surfaceDestroyed();
    }

    @Override
    public void run() {
        while (isCanDraw) {
            long currentTime = SystemClock.currentThreadTimeMillis();
            if (currentTime - preDrawTime > SurfaceConfig.DRAW_TIME_DIFF) {
                preDrawTime = currentTime;
                draw();
            }
        }
    }

    private synchronized void draw() {
        try {
            mCanvas = sHolder.lockCanvas();
            if (null != mCanvas) {
                Set<FloatingBean> tempSet = mTransFormEventThread.getEventSet();
                if (tempSet != null && tempSet.size() > 0) {
                    // 清屏
                    mCanvas.drawColor(PixelFormat.TRANSPARENT, PorterDuff.Mode.CLEAR);

                    // 处理事件集合
                    Iterator<FloatingBean> iterator = tempSet.iterator();
                    while (iterator.hasNext()) {
                        FloatingBean fBean = iterator.next();
                        if (fBean == null) {
                            iterator.remove();
                        } else if (fBean.fromX > fBean.toX
                                && fBean.showTime < fBean.animDuration
                                && fBean.bitmap != null) {
                            mCanvas.drawBitmap(fBean.bitmap, fBean.fromX, fBean.topY, null);
                            fBean.fromX -= fBean.disDiff;
                            fBean.showTime += SurfaceConfig.DRAW_TIME_DIFF;
                        } else {
                            if (fBean.bitmap != null) {
                                fBean.bitmap.recycle();
                                fBean.bitmap = null;
                            }
                            if (fBean.vFloating != null) {
                                fBean.vFloating.updateState(ViewState.END);
                            }
                            iterator.remove();
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("FloatingContainer-E：", e.getMessage() + "");
        } finally {
            if (mCanvas != null) {
                sHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    public void addFloatingMsg(@NonNull FloatingMsg fMsg) {
        if (mTransFormEventThread != null) {
            Message message = Message.obtain();
            message.obj = fMsg;
            mTransFormEventThread.transformMsg(message);
        }
    }

    public void removeFloatingMsg(@NonNull FloatingMsg fMsg) {
        if (mTransFormEventThread != null) {
            mTransFormEventThread.remove(fMsg.vFloating);
        }
    }

    public void clear() {
        if (mTransFormEventThread != null) {
            mTransFormEventThread.clear();
        }
    }

    public abstract FLayerConfig getFLayerConfig();
}
