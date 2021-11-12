package cc.ibooker.floatinglayer.core;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;

import cc.ibooker.floatinglayer.core.anim.IAnim;
import cc.ibooker.floatinglayer.core.anim.IAnimListener;
import cc.ibooker.floatinglayer.core.combination.CombinationEnum;
import cc.ibooker.floatinglayer.core.event.FloatingEvent;
import cc.ibooker.floatinglayer.core.event.IEvent;
import cc.ibooker.floatinglayer.core.executor.FloatingExecutorProxy;
import cc.ibooker.floatinglayer.core.executor.IExecutor;
import cc.ibooker.floatinglayer.core.flayer.FLayerRegion;
import cc.ibooker.floatinglayer.core.flayer.IFloatingLayerConfig;
import cc.ibooker.floatinglayer.core.observer.ViewAnimObserver;
import cc.ibooker.floatinglayer.core.observer.ViewStateObserver;
import cc.ibooker.floatinglayer.core.observer.ViewZClickObserver;
import cc.ibooker.floatinglayer.core.vholder.IViewHolder;
import cc.ibooker.floatinglayer.core.vholder.IViewStateListener;
import cc.ibooker.floatinglayer.core.vholder.IViewZClickListener;

/**
 * @program: ZFloatingLayer
 * @description: 浮层对外支持类
 * @author: zoufengli01
 * @create: 2021-10-12 09:44
 **/
public class FloatingService implements IFloatingLayerConfig {
    private final IExecutor fExecutor;
    private final WeakReference<FloatingService> mWeak;

    public FloatingService(@NonNull Context context) {
        // 动态申请权限
        String permission = Manifest.permission.SYSTEM_ALERT_WINDOW;
        if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, 1010);
            }
        }
        fExecutor = new FloatingExecutorProxy(context);
        // 使用弱引用
        mWeak = new WeakReference<>(this);
    }

    /**
     * 设置浮层位置
     *
     * @param sX X轴开始位置
     * @param sY Y轴开始位置
     */
    @Override
    public void location(int sX, int sY) {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.fLayer().location(sX, sY);
        }
    }

    /**
     * 设置浮层大小
     *
     * @param w 宽
     * @param h 高
     */
    @Override
    public void size(int w, int h) {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.fLayer().size(w, h);
        }
    }

    /**
     * 设置显示区域
     */
    @Override
    public void region(@NonNull @FLayerRegion String region) {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.fLayer().region(region);
        }
    }

    /**
     * 设置动画时间
     *
     * @param animDuration 单位ms
     */
    @Override
    public void animDuration(int animDuration) {
        if (mWeak.get() != null && animDuration > 0) {
            mWeak.get().fExecutor.fLayer().animDuration(animDuration);
        }
    }

    /**
     * 设置子View垂直间距
     *
     * @param childVerMargin 偏移量 px
     */
    @Override
    public void childVerMargin(int childVerMargin) {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.fLayer().childVerMargin(childVerMargin);
        }
    }

    /**
     * 设置子View水平间距
     *
     * @param childHorMargin 偏移量 px
     */
    @Override
    public void childHorMargin(int childHorMargin) {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.fLayer().childHorMargin(childHorMargin);
        }
    }

    /**
     * 展示
     */
    public void show() {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.onShow();
        }
    }

    /**
     * 隐藏
     */
    public void hide() {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.onHide();
        }
    }

    /**
     * 销毁
     */
    public void destroy() {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.onDestroy();
        }
    }

    /**
     * 清屏
     */
    public void clear() {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.onClear();
        }
    }

    /**
     * 注册View状态监听
     */
    public void registerViewStateListener(@NonNull IViewStateListener listener) {
        ViewStateObserver.getInstance().registerListener(listener);
    }

    /**
     * 注册View点击事件监听
     */
    public void registerViewZClickListener(@NonNull IViewZClickListener listener) {
        ViewZClickObserver.getInstance().registerListener(listener);
    }

    /**
     * 注册View动画监听
     */
    public void registerViewAnimListener(@NonNull IAnimListener listener) {
        ViewAnimObserver.getInstance().registerListener(listener);
    }

    /**
     * 预加载ViewHolder
     *
     * @param vHolder 待加载ViewHolder
     * @param num     加载数量
     */
    public <T> void preViewHolder(@NonNull IViewHolder<T> vHolder, int num) {
        if (mWeak.get() != null) {
            mWeak.get().fExecutor.preViewHolder(vHolder, num);
        }
    }

    /**
     * 发送事件，采用默认动画组合一
     *
     * @param event 事件
     */
    public <T> void send(IEvent<T> event) {
        if (event != null) {
            send(event, CombinationEnum.COMBINATION_ANIMATOR_THREE);
        }
    }

    /**
     * 发送事件，采用默认动画类型（从右向左）
     *
     * @param event           事件
     * @param combinationEnum 动画组合
     */
    public <T> void send(IEvent<T> event, @NonNull CombinationEnum combinationEnum) {
        if (event != null) {
            mWeak.get().fExecutor.execute(new FloatingEvent<>(event, combinationEnum));
        }
    }

    /**
     * 发送事件
     *
     * @param event 事件
     * @param iAnim 自定义动画，一定要实现动画回调方法
     */
    public <T> void send(IEvent<T> event, @NonNull IAnim iAnim) {
        if (event != null && mWeak.get() != null) {
            mWeak.get().fExecutor.execute(new FloatingEvent<>(event, iAnim));
        }
    }

    /**
     * 构造器，简化执行方法
     */
    public static class Proxy<T> {
        private Context context;
        private IViewHolder<T> vHolder;
        private FloatingService floatingService;

        private Proxy() {
        }

        public Proxy(Context context, @NonNull IViewHolder<T> vHolder) {
            this.context = context;
            this.vHolder = vHolder;
        }

        @NonNull
        private IEvent<T> createEvent(@NonNull final T data) {
            return new IEvent<T>() {
                @Override
                public IViewHolder<T> vHolder() {
                    return vHolder;
                }

                @Override
                public T data() {
                    return data;
                }
            };
        }

        @NonNull
        public Proxy<T> location(int sX, int sY) {
            create().location(sX, sY);
            return this;
        }

        public Proxy<T> size(int w, int h) {
            create().size(w, h);
            return this;
        }

        public Proxy<T> region(@NonNull @FLayerRegion String region) {
            create().region(region);
            return this;
        }

        public Proxy<T> animDuration(int animDuration) {
            create().animDuration(animDuration);
            return this;
        }

        public Proxy<T> childVerMargin(int childVerMargin) {
            create().childVerMargin(childVerMargin);
            return this;
        }

        public Proxy<T> childHorMargin(int childHorMargin) {
            create().childHorMargin(childHorMargin);
            return this;
        }

        public Proxy<T> hide() {
            create().hide();
            return this;
        }

        public Proxy<T> show() {
            create().show();
            return this;
        }

        public Proxy<T> destroy() {
            create().destroy();
            return this;
        }

        public Proxy<T> clear() {
            create().clear();
            return this;
        }

        public Proxy<T> preViewHolder(@NonNull IViewHolder<T> vHolder, int num) {
            create().preViewHolder(vHolder, num);
            return this;
        }

        @NonNull
        public Proxy<T> send(@NonNull T data) {
            create().send(createEvent(data));
            return this;
        }

        @NonNull
        public Proxy<T> send(@NonNull T data, @NonNull CombinationEnum combinationEnum) {
            create().send(createEvent(data), combinationEnum);
            return this;
        }

        @NonNull
        public Proxy<T> send(@NonNull T data, IAnim iAnim) {
            create().send(createEvent(data), iAnim);
            return this;
        }

        @NonNull
        public FloatingService create() {
            if (floatingService == null) {
                floatingService = new FloatingService(context);
            }
            return floatingService;
        }
    }
}
