package cc.ibooker.floatinglayer.core.anim.executor.surface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.anim.IAnimListener;
import cc.ibooker.floatinglayer.core.anim.executor.scroller.ZView;
import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 自定义SurfaceView
 * @author: zoufengli01
 * @create: 2021-10-25 10:24
 * TODO 这里的SurfaceView并未写好，后期升级方案，一个SurfaceView实例多个线程执行绘制不同View
 **/
public class ZSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private final SurfaceHolder sHolder;
    /*标记是否能够绘制*/
    private boolean isCanDraw;
    /*Surface画布*/
    private Canvas mCanvas;
    /*动画监听器*/
    private IAnimListener mListener;
    /*待处理View*/
    private View mView;
    /*mView对应位图*/
    private Bitmap mBitmap;
    /*X轴开始位置*/
    private float fromX;
    /*X轴结束位置*/
    private float toX;
    /*每次绘制偏移量*/
    private float disDiff;
    /*绘制时间间隔*/
    private final int timeDiff = 10;
    /*记录上一次绘制时间*/
    private long preDrawTime;

    public void startAnim(@NonNull View view,
                          @NonNull FLayerConfig fLayerConfig,
                          @NonNull IAnimListener listener) {
        Log.d("ZSurfaceView", "startAnim:" + isCanDraw);
        this.addView(view);
        int width = view.getMeasuredWidth();
        this.fromX = fLayerConfig.startX + fLayerConfig.width - 10;
        this.toX = fLayerConfig.startX - width;
        this.disDiff = 2 * (fromX - toX) / fLayerConfig.animDuration * timeDiff;
        this.mListener = listener;
        post(new Runnable() {
            @Override
            public void run() {
                mListener.onAnimStart(mView);
            }
        });
    }

    public ZSurfaceView(Context context) {
        this(context, null);
    }

    public ZSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setZOrderOnTop(true);
        // 获取SurfaceHolder
        sHolder = getHolder();
        sHolder.setFormat(PixelFormat.TRANSPARENT);
        sHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("ZSurfaceView", "surfaceCreated:" + isCanDraw);
        isCanDraw = true;
        new Thread(this).start();
    }

    /**
     * 添加View
     *
     * @param view 被添加View一定是子View，而非包装View
     */
    public void addView(@NonNull View view) {
        // 获取子View
        if (view instanceof ZView) {
            view = ((ZView) view).getView();
        } else if (view instanceof ZSurfaceView) {
            view = ((ZSurfaceView) view).getView();
        }
        // 移除子View的父View
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        // 设置子View
        this.mView = view;
    }

    public View getView() {
        return mView;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("ZSurfaceView", "surfaceChanged:" + isCanDraw);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("ZSurfaceView", "surfaceDestroyed:" + isCanDraw);
        isCanDraw = false;
    }

    @Override
    public void run() {
        while (isCanDraw) {
            long currentTime = SystemClock.currentThreadTimeMillis();
            if (currentTime - preDrawTime > timeDiff) {
                preDrawTime = currentTime;
                draw();
            }
        }
    }

    private void draw() {
        synchronized (sHolder) {
            if (fromX > toX && mView != null) {
                try {
                    if (mBitmap == null || mBitmap.getByteCount() <= 0) {
                        mBitmap = viewToBitmap(mView);
                    }
                    mCanvas = sHolder.lockCanvas();
                    if (null != mCanvas && null != mBitmap) {
                        mCanvas.drawColor(PixelFormat.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        mCanvas.drawBitmap(mBitmap, fromX, 0, null);
                        fromX -= disDiff;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mCanvas != null) {
                        sHolder.unlockCanvasAndPost(mCanvas);
                    }
                }
            } else {
                if (mBitmap != null) {
                    mBitmap.recycle();
                }
                if (mListener != null) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            mListener.onAnimEnd(mView);
                        }
                    });
                }
            }
        }
    }

    /**
     * View转Bitmap
     *
     * @param view 目标View
     */
    private Bitmap viewToBitmap(@NonNull View view) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();

        view.layout(0, 0, measuredWidth, measuredHeight);

        Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}