package cc.ibooker.floatinglayer.core.anim.executor;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Scroller;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.anim.AnimFunStyle;
import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;
import cc.ibooker.floatinglayer.core.vholder.ViewTag;

/**
 * @program: ZFloatingLayer
 * @description: 动画工厂
 * @author: zoufengli01
 * @create: 2021-10-12 09:55
 **/
public class AnimFactory {

    public static Animator getAnimator(@NonNull @AnimFunStyle String animFunStyle,
                                       @NonNull ViewFloating viewFloating,
                                       @NonNull FLayerConfig fLayerConfig) {
        Animator animator = null;
        if (viewFloating.view != null) {
            View view = viewFloating.view;
            switch (animFunStyle) {
                case AnimFunStyle.RIGHT_TO_LEFT:
                    int width = view.getMeasuredWidth();
                    int fromX = fLayerConfig.startX + fLayerConfig.width - 10;
                    int toX = fLayerConfig.startX - width;

                    int middleX = (fromX - toX) / 2 + toX;
                    Keyframe zK0 = Keyframe.ofFloat(0f, fromX);
                    Keyframe zK1 = Keyframe.ofFloat(0.5f, middleX);
                    Keyframe zK2 = Keyframe.ofFloat(1f, toX);
                    PropertyValuesHolder zPvh = PropertyValuesHolder.ofKeyframe("translationX", zK0, zK1, zK2);
                    animator = ObjectAnimator.ofPropertyValuesHolder(view, zPvh);

//                    animator = ObjectAnimator.ofFloat(view, "translationX", fromX, toX);
                    break;
                case AnimFunStyle.SHOW_TO_HIDE:
                    Keyframe sK0 = Keyframe.ofFloat(0f, 0f);
                    Keyframe sK1 = Keyframe.ofFloat(0.5f, 0.5f);
                    Keyframe sK2 = Keyframe.ofFloat(1f, 1f);
                    PropertyValuesHolder sPvh = PropertyValuesHolder.ofKeyframe("alpha", sK0, sK1, sK2);
                    animator = ObjectAnimator.ofPropertyValuesHolder(view, sPvh);

//                    animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
                    break;
            }
            if (animator != null) {
                if (viewFloating.viewTag == null) {
                    viewFloating.viewTag = new ViewTag();
                }
                viewFloating.viewTag.animator = animator;
            }
        }
        return animator;
    }

    public static Animation getAnimation(@NonNull @AnimFunStyle String animFunStyle,
                                         @NonNull ViewFloating viewFloating,
                                         @NonNull FLayerConfig fLayerConfig) {
        Animation animation = null;
        if (viewFloating.view != null) {
            View view = viewFloating.view;
            switch (animFunStyle) {
                case AnimFunStyle.RIGHT_TO_LEFT:
                    int width = view.getMeasuredWidth();
                    int fromX = fLayerConfig.startX + fLayerConfig.width - 10;
                    int toX = fLayerConfig.startX - width;
                    animation = new TranslateAnimation(fromX, toX, 0, 0);
                    break;
                case AnimFunStyle.SHOW_TO_HIDE:
                    animation = new AlphaAnimation(1f, 0f);
                    break;
            }
            if (animation != null) {
                if (viewFloating.viewTag == null) {
                    viewFloating.viewTag = new ViewTag();
                }
                viewFloating.viewTag.animation = animation;
            }
        }
        return animation;
    }

    public static Scroller getScroller(@NonNull @AnimFunStyle String animFunStyle,
                                       @NonNull ViewFloating viewFloating,
                                       @NonNull Interpolator interpolator) {
        Scroller scroller = null;
        if (viewFloating.view != null) {
            View view = viewFloating.view;
            if (AnimFunStyle.RIGHT_TO_LEFT.equals(animFunStyle)) {
                scroller = new Scroller(view.getContext(), interpolator);
            }
            if (scroller != null) {
                if (viewFloating.viewTag == null) {
                    viewFloating.viewTag = new ViewTag();
                }
                viewFloating.viewTag.scroller = scroller;
            }
        }
        return scroller;
    }

}
