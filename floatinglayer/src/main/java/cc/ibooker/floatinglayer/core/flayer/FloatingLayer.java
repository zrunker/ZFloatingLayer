package cc.ibooker.floatinglayer.core.flayer;


import static cc.ibooker.floatinglayer.core.util.ConstantUtil.MAX_VIEW_NUM;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.flayer.view.FloatingLayout;
import cc.ibooker.floatinglayer.core.flayer.view.strategy.LocationStrategy;
import cc.ibooker.floatinglayer.core.observer.ViewStateObserver;
import cc.ibooker.floatinglayer.core.vholder.ViewState;


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
    private FloatingLayout fLayout;
    private FLayerConfig fLayerConfig;
    /*子View的LayoutParams*/
    private ViewGroup.MarginLayoutParams cLayoutParams;
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
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                wParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//            } else {
//                wParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//            }
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
     * 初始化父View
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

        this.fLayout = new FloatingLayout(context);

        show();
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
                    && fLayout.getParent() == null) {
                if (activity.getWindow().isActive()
                        && !fLayout.isAttachedToWindow()) {
                    this.wm.addView(fLayout, wParams);
                } else {
                    Looper.myQueue().addIdleHandler(() -> {
                        show();
                        return false;
                    });
                }
            }
        }
    }

    /**
     * 隐藏
     */
    @Override
    public void hide() {
        Looper.myQueue().addIdleHandler(() -> {
            if (fLayout.isAttachedToWindow()) {
                this.wm.removeView(fLayout);
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
        this.fLayout.removeAllViews();
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
            if (fLayout.isAttachedToWindow()) {
                this.fLayerConfig.startX = sX;
                this.fLayerConfig.startY = sY;
                this.wParams.x = fLayerConfig.startX;
                this.wParams.y = fLayerConfig.startY;
                this.wm.updateViewLayout(fLayout, wParams);
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
            if (fLayout.isAttachedToWindow()) {
                this.fLayerConfig.width = w;
                this.fLayerConfig.height = h;
                this.wParams.width = fLayerConfig.width;
                this.wParams.height = fLayerConfig.height;
                this.wm.updateViewLayout(fLayout, wParams);
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
            if (fLayout.isAttachedToWindow()) {
                this.fLayerConfig.region = region;
                switch (this.fLayerConfig.region) {
                    case FLayerRegion.SCREEN_FULL:
                        this.wParams.verticalMargin = 0;
                        break;
                    case FLayerRegion.SCREEN_CENTER:
                        this.wParams.verticalMargin = (float) screenHeight / 4;
                        break;
                }
                this.wm.updateViewLayout(fLayout, wParams);
            } else {
                region(region);
            }
            return false;
        });
    }

    @Override
    public void animDuration(int animDuration) {
        if (animDuration > 0) {
            this.fLayerConfig.animDuration = animDuration;
        }
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
     * 添加View
     *
     * @param view 待处理View
     */
    @Override
    public void addView(@NonNull View view) {
        if (view.getParent() != fLayout || fLayout.getChildCount() > MAX_VIEW_NUM) {
            // 移除View
            removeView(view);
            // 设置LayoutParams
            if (cLayoutParams == null) {
                cLayoutParams = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cLayoutParams.setMargins(
                        fLayerConfig.childHorMargin,
                        fLayerConfig.childVerMargin,
                        fLayerConfig.childHorMargin,
                        fLayerConfig.childVerMargin);
            }
            this.fLayout.addView(view, cLayoutParams);
            // 发送通知
            ViewStateObserver.getInstance().onAdd(view);
        }
    }

    /**
     * 移除View
     *
     * @param vFloating 待处理View
     */
    @Override
    public void removeView(@NonNull ViewFloating vFloating) {
        if (vFloating.view != null && vFloating.viewTag.viewState == ViewState.END) {
            removeView(vFloating.view);
        }
    }

    private void removeView(View view) {
        if (view.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
            // 发送通知
            ViewStateObserver.getInstance().onRemove(view);
        }
    }

    /**
     * 转化子控件
     *
     * @param view 待处理View
     */
    @Override
    public void resetChildViewLocation(@NonNull View view,
                                       @LocationStrategy int locationStrategy) {
        this.fLayout.resetChildViewLocation(view, fLayerConfig, locationStrategy);
    }
}
