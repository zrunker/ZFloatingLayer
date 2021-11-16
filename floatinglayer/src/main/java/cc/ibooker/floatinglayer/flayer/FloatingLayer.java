package cc.ibooker.floatinglayer.flayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.container.SurfaceContainer;
import cc.ibooker.floatinglayer.executor.FloatingMsg;

/**
 * @program: ZFloatingLayer
 * @description: 浮层管理类
 * @author: zoufengli01
 * @create: 2021-10-12 10:41
 **/
public class FloatingLayer implements IFloatingLayer {
    private final Context context;
    private WindowManager wm;
    private WindowManager.LayoutParams wParams;
    private FLayerConfig fLayerConfig;
    /*容器View*/
    private SurfaceContainer sContainer;
    /*屏幕高度*/
    private int screenHeight;

    public FloatingLayer(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        if (fLayerConfig == null) {
            fLayerConfig = new FLayerConfig();
        }
        if (wm == null) {
            wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        if (wParams == null) {
            wParams = new WindowManager.LayoutParams();
            // 设置type
            wParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL;
            // 设置flags
            wParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
            wParams.gravity = Gravity.START | Gravity.TOP;
            // 背景设置成透明
            wParams.format = PixelFormat.TRANSPARENT;
        }
        initView();
    }

    /**
     * 初始化容器View
     */
    private void initView() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        this.wm.getDefaultDisplay().getMetrics(outMetrics);
        this.screenHeight = outMetrics.heightPixels;
        this.fLayerConfig.height = screenHeight;
        this.fLayerConfig.width = outMetrics.widthPixels;
        this.fLayerConfig.startX = 0;
        this.fLayerConfig.startY = 0;

        this.wParams.x = fLayerConfig.startX;
        this.wParams.y = fLayerConfig.startY;
        this.wParams.width = fLayerConfig.width;
        this.wParams.height = fLayerConfig.height;
        this.wParams.horizontalMargin = 0;
        this.wParams.verticalMargin = 0;

        this.sContainer = new SurfaceContainer(context) {
            @Override
            public FLayerConfig getFLayerConfig() {
                return fLayerConfig;
            }
        };

        this.show();
    }

    /**
     * 展示
     */
    @Override
    public void show() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()
                    && !activity.isDestroyed()
                    && activity.getWindow() != null
                    && sContainer.getParent() == null
                    && activity.getWindow().isActive()
                    && !sContainer.isAttachedToWindow()) {
                Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                    @Override
                    public boolean queueIdle() {
                        FloatingLayer.this.wm.addView(sContainer, wParams);
                        return false;
                    }
                });
            }
        }
    }

    /**
     * 隐藏
     */
    @Override
    public void hide() {
        Looper.myQueue().addIdleHandler(() -> {
            if (sContainer.isAttachedToWindow()) {
                this.wm.removeView(sContainer);
            }
            return false;
        });
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        this.clear();
        this.hide();
    }

    /**
     * 清屏
     */
    @Override
    public void clear() {
        this.sContainer.clear();
    }

    /**
     * 设置浮层位置
     *
     * @param sX X轴开始位置
     * @param sY Y轴开始位置
     */
    @Override
    public void location(int sX, int sY) {
        Looper.myQueue().addIdleHandler(() -> {
            if (sContainer.isAttachedToWindow()) {
                this.fLayerConfig.startX = sX;
                this.fLayerConfig.startY = sY;
                this.wParams.x = fLayerConfig.startX;
                this.wParams.y = fLayerConfig.startY;
                this.wm.updateViewLayout(sContainer, wParams);
            } else {
                location(sX, sY);
            }
            return false;
        });
    }

    /**
     * 设置浮层大小
     *
     * @param w 宽
     * @param h 高
     */
    @Override
    public void size(int w, int h) {
        if (w < 0 || h < 0) {
            return;
        }
        Looper.myQueue().addIdleHandler(() -> {
            if (sContainer.isAttachedToWindow()) {
                this.fLayerConfig.width = w;
                this.fLayerConfig.height = h;
                this.wParams.width = fLayerConfig.width;
                this.wParams.height = fLayerConfig.height;
                this.wm.updateViewLayout(sContainer, wParams);
            } else {
                size(w, h);
            }
            return false;
        });
    }

    /**
     * 设置显示区域
     */
    @Override
    public void region(@NonNull @FLayerRegion String region) {
        Looper.myQueue().addIdleHandler(() -> {
            if (sContainer.isAttachedToWindow()) {
                this.fLayerConfig.region = region;
                switch (this.fLayerConfig.region) {
                    case FLayerRegion.SCREEN_FULL:
                        this.wParams.verticalMargin = 0;
                        break;
                    case FLayerRegion.SCREEN_CENTER:
                        this.wParams.verticalMargin = (float) screenHeight / 4;
                        break;
                }
                this.wm.updateViewLayout(sContainer, wParams);
            } else {
                region(region);
            }
            return false;
        });
    }

    @Override
    public void childVerMargin(int childVerMargin) {
        if (childVerMargin > 0) {
            this.fLayerConfig.childVerMargin = childVerMargin;
        }
    }

    @Override
    public void childHorMargin(int childHorMargin) {
        if (childHorMargin > 0) {
            this.fLayerConfig.childHorMargin = childHorMargin;
        }
    }

    @NonNull
    @Override
    public FLayerConfig fLayerConfig() {
        return fLayerConfig;
    }

    /**
     * 添加浮层消息
     *
     * @param fMsg 待添加数据
     */
    @Override
    public void addFloatingMsg(@NonNull FloatingMsg fMsg) {
        this.sContainer.addFloatingMsg(fMsg);
    }

    /**
     * 移除浮层消息
     *
     * @param fMsg 待添加数据
     */
    @Override
    public void removeFloatingMsg(@NonNull FloatingMsg fMsg) {
        this.sContainer.removeFloatingMsg(fMsg);
    }
}
