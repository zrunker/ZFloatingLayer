package cc.ibooker.floatinglayer.core.anim.executor;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.anim.IAnimListener;
import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 补间动画执行类
 * @author: zoufengli01
 * @create: 2021-10-14 19:12
 **/
public class AnimationExecutor extends AbsAnimExecutor {

    public AnimationExecutor(String animFunStyle) {
        super(animFunStyle);
    }

    @Override
    public void execute(@NonNull ViewFloating viewFloating,
                        @NonNull FLayerConfig fLayerConfig,
                        @NonNull final IAnimListener listener) {
        final View view = viewFloating.view;
        if (view != null) {
            Animation animation = AnimFactory.getAnimation(animFunStyle, viewFloating, fLayerConfig);
            if (animation != null) {
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        listener.onAnimStart(view);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        listener.onAnimEnd(view);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animation.setInterpolator(new LinearInterpolator());
                animation.setDuration(fLayerConfig.animDuration);
                animation.setFillAfter(true);
                view.startAnimation(animation);
            }
        }
    }
}
