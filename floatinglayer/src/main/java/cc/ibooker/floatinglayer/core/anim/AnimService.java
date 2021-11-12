package cc.ibooker.floatinglayer.core.anim;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.anim.executor.AbsAnimExecutor;
import cc.ibooker.floatinglayer.core.anim.executor.AnimationExecutor;
import cc.ibooker.floatinglayer.core.anim.executor.AnimatorExecutor;
import cc.ibooker.floatinglayer.core.anim.executor.scroller.ScrollerExecutor;
import cc.ibooker.floatinglayer.core.anim.executor.surface.SurfaceExecutor;
import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.combination.CombinationEnum;
import cc.ibooker.floatinglayer.core.event.FloatingEvent;
import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;
import cc.ibooker.floatinglayer.core.observer.ViewAnimObserver;
import cc.ibooker.floatinglayer.core.vholder.ViewState;

/**
 * @program: ZFloatingLayer
 * @description: 动画执行类
 * @author: zoufengli01
 * @create: 2021-10-18 21:21
 **/
public abstract class AnimService {

    public <T> void executeAnim(@NonNull ViewFloating viewFloating,
                                @NonNull FloatingEvent<T> fEvent,
                                @NonNull FLayerConfig fLayerConfig) {
        if (fEvent.anim == null) {
            // 执行设定动画
            AbsAnimExecutor animExecutor = null;
            // 动画组合
            CombinationEnum combinationEnum = fEvent.combinationEnum;
            // 动画类型
            String animFunStyle = combinationEnum.getAnimFunStyle();
            // 动画种类
            String animType = combinationEnum.getAnimType();
            // 初始化动画执行类
            switch (animType) {
                case AnimType.ANIMATOR:
                    animExecutor = new AnimatorExecutor(animFunStyle);
                    break;
                case AnimType.ANIMATION:
                    animExecutor = new AnimationExecutor(animFunStyle);
                    break;
                case AnimType.SCROLLER:
                    animExecutor = new ScrollerExecutor(animFunStyle);
                    break;
                case AnimType.SURFACE:
                    animExecutor = new SurfaceExecutor(animFunStyle);
                    break;
            }
            if (animExecutor != null) {
                // 重置View位置
                animInitBack(viewFloating, fEvent);
                // 启动动画
                startAnim(viewFloating, fEvent, fLayerConfig, animExecutor);
            }
        } else {
            // 执行自定义动画
            fEvent.anim.executeAnim(viewFloating.view, fLayerConfig, getAnimListener(viewFloating, fEvent));
        }
    }

    /**
     * 动画初始化回调
     */
    protected abstract void animInitBack(@NonNull ViewFloating viewFloating,
                                         @NonNull FloatingEvent<?> fEvent);

    /**
     * 动画开始回调
     */
    protected abstract void animStartBack(@NonNull ViewFloating viewFloating,
                                          @NonNull FloatingEvent<?> fEvent);

    /**
     * 动画结束回调
     */
    protected abstract void animEndBack(@NonNull ViewFloating viewFloating,
                                        @NonNull FloatingEvent<?> fEvent);

    /**
     * 启动动画
     */
    private void startAnim(@NonNull ViewFloating viewFloating,
                           @NonNull FloatingEvent<?> fEvent,
                           @NonNull FLayerConfig fLayerConfig,
                           @NonNull AbsAnimExecutor animExecutor) {
        animExecutor.execute(viewFloating, fLayerConfig, getAnimListener(viewFloating, fEvent));
    }

    /**
     * 获取View回调监听
     */
    private IAnimListener getAnimListener(@NonNull final ViewFloating viewFloating,
                                          @NonNull final FloatingEvent<?> fEvent) {
        return new IAnimListener() {

            @Override
            public void onAnimStart(View view) {
                Log.d("AnimService", "动画开始");
                // 动画开始回调
                animStartBack(viewFloating, fEvent);
                // 设置ViewTag
                viewFloating.updateViewState(ViewState.START);
                // 发送通知
                ViewAnimObserver.getInstance().onAnimStart(viewFloating.view);
            }

            @Override
            public void onAnimEnd(View view) {
                Log.d("AnimService", "动画结束");
                // 动画结束回调
                animEndBack(viewFloating, fEvent);
                // 设置ViewTag
                viewFloating.updateViewState(ViewState.END);
                // 发送通知
                ViewAnimObserver.getInstance().onAnimEnd(viewFloating.view);
            }
        };
    }
}
