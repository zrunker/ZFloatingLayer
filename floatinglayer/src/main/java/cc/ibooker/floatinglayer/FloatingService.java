package cc.ibooker.floatinglayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;

import cc.ibooker.floatinglayer.combination.CombinationEnum;
import cc.ibooker.floatinglayer.event.FloatingEvent;
import cc.ibooker.floatinglayer.event.IEvent;
import cc.ibooker.floatinglayer.executor.FloatingExecutorProxy;
import cc.ibooker.floatinglayer.executor.IExecutor;
import cc.ibooker.floatinglayer.flayer.FLayerRegion;
import cc.ibooker.floatinglayer.flayer.IFloatingLayerConfig;
import cc.ibooker.floatinglayer.observer.IAnimListener;
import cc.ibooker.floatinglayer.observer.IViewStateListener;
import cc.ibooker.floatinglayer.observer.ViewAnimObserver;
import cc.ibooker.floatinglayer.observer.ViewStateObserver;
import cc.ibooker.floatinglayer.util.Config;
import cc.ibooker.floatinglayer.vholder.IViewHolder;

/**
 * @program: ZFloatingLayer
 * @description: 浮层对外支持类
 * @author: zoufengli01
 * @create: 2021-10-12 09:44
 **/
public class FloatingService implements IFloatingLayerConfig {
    private final IExecutor fExecutor;

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
    }

    /**
     * 设置浮层位置
     *
     * @param sX X轴开始位置
     * @param sY Y轴开始位置
     */
    @Override
    public void location(int sX, int sY) {
        fExecutor.fLayer().location(sX, sY);
    }

    /**
     * 设置浮层大小
     *
     * @param w 宽
     * @param h 高
     */
    @Override
    public void size(int w, int h) {
        fExecutor.fLayer().size(w, h);
    }

    /**
     * 设置显示区域
     */
    @Override
    public void region(@NonNull @FLayerRegion String region) {
        fExecutor.fLayer().region(region);
    }

    /**
     * 设置动画时长
     *
     * @param animDuration 单位ms
     */
    public void defaultAnimDuration(int animDuration) {
        if (animDuration > 0) {
            Config.defaultAnimDuration = animDuration;
        }
    }

    /**
     * 是否开启缓存
     *
     * @param isOpenCache 是否开启缓存
     */
    public void isOpenCache(boolean isOpenCache) {
        Config.isOpenCache = isOpenCache;
    }

    /**
     * 设置子View垂直间距
     *
     * @param childVerMargin 偏移量 px
     */
    @Override
    public void childVerMargin(int childVerMargin) {
        fExecutor.fLayer().childVerMargin(childVerMargin);
    }

    /**
     * 设置子View水平间距
     *
     * @param childHorMargin 偏移量 px
     */
    @Override
    public void childHorMargin(int childHorMargin) {
        fExecutor.fLayer().childHorMargin(childHorMargin);
    }

    /**
     * 展示
     */
    public void show() {
        fExecutor.onShow();
    }

    /**
     * 隐藏
     */
    public void hide() {
        fExecutor.onHide();
    }

    /**
     * 销毁
     */
    public void destroy() {
        fExecutor.onDestroy();
    }

    /**
     * 清屏
     */
    public void clear() {
        fExecutor.onClear();
    }

    /**
     * 注册View动画监听
     */
    public void registerViewAnimListener(@NonNull IAnimListener listener) {
        ViewAnimObserver.getInstance().registerListener(listener);
    }

    /**
     * 注册View状态监听
     */
    public void registerViewStateListener(@NonNull IViewStateListener listener) {
        ViewStateObserver.getInstance().registerListener(listener);
    }

    /**
     * 预加载ViewHolder
     *
     * @param vHolder 待加载ViewHolder
     * @param num     加载数量
     */
    public <T> void preViewHolder(@NonNull IViewHolder<T> vHolder, int num) {
        fExecutor.preViewHolder(vHolder, num);
    }

    /**
     * 发送事件，采用默认形式
     *
     * @param event 事件
     */
    public <T> void send(IEvent<T> event) {
        if (event != null) {
            fExecutor.execute(new FloatingEvent<>(event));
        }
    }

    /**
     * 发送事件，设置动画组合
     *
     * @param event           事件
     * @param combinationEnum 动画组合
     */
    public <T> void send(IEvent<T> event, @NonNull CombinationEnum combinationEnum) {
        if (event != null) {
            fExecutor.execute(new FloatingEvent<>(event, combinationEnum));
        }
    }

    /**
     * 发送事件，设置动画事件
     *
     * @param event        事件
     * @param animDuration 动画时长
     */
    public <T> void send(IEvent<T> event, int animDuration) {
        if (event != null) {
            fExecutor.execute(new FloatingEvent<>(event, animDuration));
        }
    }

    /**
     * 发送事件
     *
     * @param event           事件
     * @param combinationEnum 动画组合
     * @param animDuration    动画时长
     */
    public <T> void send(IEvent<T> event, @NonNull CombinationEnum combinationEnum, int animDuration) {
        if (event != null) {
            fExecutor.execute(new FloatingEvent<>(event, combinationEnum, animDuration));
        }
    }

    /**
     * 代理类，简化执行方法
     */
    public static class Proxy<T> {
        private IViewHolder<T> vHolder;
        private FloatingService floatingService;
        // FloatingService弱引用
        private WeakReference<FloatingService> mWeak;

        private Proxy() {
        }

        public Proxy(Context context, @NonNull IViewHolder<T> vHolder) {
            this.vHolder = vHolder;
            this.floatingService = new FloatingService(context);
            this.mWeak = new WeakReference<>(floatingService);
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
            fService().location(sX, sY);
            return this;
        }

        public Proxy<T> size(int w, int h) {
            fService().size(w, h);
            return this;
        }

        public Proxy<T> region(@NonNull @FLayerRegion String region) {
            fService().region(region);
            return this;
        }

        public Proxy<T> defaultAnimDuration(int animDuration) {
            fService().defaultAnimDuration(animDuration);
            return this;
        }

        public Proxy<T> isOpenCache(boolean isOpenCache) {
            fService().isOpenCache(isOpenCache);
            return this;
        }

        public Proxy<T> childVerMargin(int childVerMargin) {
            fService().childVerMargin(childVerMargin);
            return this;
        }

        public Proxy<T> childHorMargin(int childHorMargin) {
            fService().childHorMargin(childHorMargin);
            return this;
        }

        public Proxy<T> hide() {
            fService().hide();
            return this;
        }

        public Proxy<T> show() {
            fService().show();
            return this;
        }

        public Proxy<T> destroy() {
            fService().destroy();
            return this;
        }

        public Proxy<T> clear() {
            fService().clear();
            return this;
        }

        public Proxy<T> preViewHolder(@NonNull IViewHolder<T> vHolder, int num) {
            fService().preViewHolder(vHolder, num);
            return this;
        }

        @NonNull
        public Proxy<T> send(@NonNull T data) {
            fService().send(createEvent(data));
            return this;
        }

        @NonNull
        public Proxy<T> send(@NonNull T data, @NonNull CombinationEnum combinationEnum) {
            fService().send(createEvent(data), combinationEnum);
            return this;
        }

        @NonNull
        public Proxy<T> send(@NonNull T data, int animDuration) {
            fService().send(createEvent(data), animDuration);
            return this;
        }

        @NonNull
        public Proxy<T> send(@NonNull T data, @NonNull CombinationEnum combinationEnum, int animDuration) {
            fService().send(createEvent(data), combinationEnum, animDuration);
            return this;
        }

        private FloatingService fService() {
            if (mWeak.get() == null) {
                mWeak = new WeakReference<>(floatingService);
            }
            return mWeak.get();
        }
    }
}
