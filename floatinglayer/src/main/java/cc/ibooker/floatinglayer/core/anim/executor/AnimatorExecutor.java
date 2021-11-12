package cc.ibooker.floatinglayer.core.anim.executor;

import android.animation.Animator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.anim.IAnimListener;
import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;


/**
 * @program: ZFloatingLayer
 * @description: 属性动画执行类
 * @author: zoufengli01
 * @create: 2021-10-12 11:24
 **/
public class AnimatorExecutor extends AbsAnimExecutor {

    public AnimatorExecutor(String animFunStyle) {
        super(animFunStyle);
    }

    @Override
    public void execute(@NonNull ViewFloating viewFloating,
                        @NonNull FLayerConfig fLayerConfig,
                        @NonNull final IAnimListener listener) {
        final View view = viewFloating.view;
        if (view != null) {
            Animator animator = AnimFactory.getAnimator(animFunStyle, viewFloating, fLayerConfig);
            if (animator != null) {
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        listener.onAnimStart(view);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listener.onAnimEnd(view);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        listener.onAnimEnd(view);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(fLayerConfig.animDuration);
                animator.start();
            }
        }
    }

}
