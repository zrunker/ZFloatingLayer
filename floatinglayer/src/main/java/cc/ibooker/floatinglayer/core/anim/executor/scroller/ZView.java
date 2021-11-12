package cc.ibooker.floatinglayer.core.anim.executor.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cc.ibooker.floatinglayer.core.anim.IAnimListener;
import cc.ibooker.floatinglayer.core.anim.executor.surface.ZSurfaceView;

/**
 * View包装类
 *
 * @author zoufengli01
 */
public class ZView extends FrameLayout {
    private Scroller mScroller;
    private IAnimListener iAnimListener;

    public ZView(@NonNull Context context) {
        this(context, null);
    }

    public ZView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 添加View
     *
     * @param view 被添加View一定是子View，而非包装View
     */
    @Override
    public void addView(View view) {
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
        // 移除当前View的子View
        if (getChildCount() > 0) {
            removeAllViews();
        }
        // 添加唯一子View
        view.setVisibility(View.VISIBLE);
        super.addView(view);
    }

    public View getView() {
        if (getChildCount() > 0) {
            return getChildAt(0);
        }
        return null;
    }

    public ZView setScroller(@NonNull Scroller mScroller) {
        this.mScroller = mScroller;
        this.mScroller.forceFinished(true);
        return this;
    }

    public ZView setAnimListener(IAnimListener animListener) {
        this.iAnimListener = animListener;
        return this;
    }

    public ZView startScroll(int startX, int startY, int dx, int dy, int duration) {
        if (mScroller != null) {
            mScroller.startScroll(startX, startY, dx, dy, duration);
            invalidate();
            if (iAnimListener != null) {
                iAnimListener.onAnimStart(this);
            }
        }
        return this;
    }

    @Override
    public void computeScroll() {
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                int currX = mScroller.getCurrX();
                int currY = mScroller.getCurrY();
                scrollTo(currX, currY);
                if (currX == getScrollX() && currY == getScrollY()) {
                    postInvalidate();
                }
            } else {
                mScroller.abortAnimation();
                if (iAnimListener != null) {
                    iAnimListener.onAnimEnd(this);
                }
            }
        }
    }
}