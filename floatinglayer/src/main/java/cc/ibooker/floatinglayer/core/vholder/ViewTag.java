package cc.ibooker.floatinglayer.core.vholder;

import android.animation.Animator;
import android.view.animation.Animation;
import android.widget.Scroller;

/**
 * @program: ZFloatingLayer
 * @description: View-Tag
 * @author: zoufengli01
 * @create: 2021-10-12 19:32
 **/
public class ViewTag {

    @ViewState
    public int viewState;

    public Animator animator;

    public Animation animation;

    public Scroller scroller;

    /**
     * 停止动画
     */
    public void stopAnim() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
        if (scroller != null) {
            scroller.abortAnimation();
            scroller = null;
        }
    }

    @Override
    public String toString() {
        return "ViewTag{" +
                "viewState=" + viewState +
                ", animator=" + animator +
                ", animation=" + animation +
                ", scroller=" + scroller +
                '}';
    }
}
