package cc.ibooker.floatinglayer.core.anim.executor.scroller;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.anim.IAnimListener;
import cc.ibooker.floatinglayer.core.anim.executor.AbsAnimExecutor;
import cc.ibooker.floatinglayer.core.anim.executor.AnimFactory;
import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;
import cc.ibooker.floatinglayer.core.flayer.view.FloatingLayout;
import cc.ibooker.floatinglayer.core.util.ViewUtil;

/**
 * @program: ZFloatingLayer
 * @description: Scroller执行类
 * @author: zoufengli01
 * @create: 2021-10-19 15:26
 **/
public class ScrollerExecutor extends AbsAnimExecutor {

    public ScrollerExecutor(String animFunStyle) {
        super(animFunStyle);
    }

    @Override
    public void execute(@NonNull ViewFloating viewFloating,
                        @NonNull FLayerConfig fLayerConfig,
                        @NonNull IAnimListener listener) {
        View view = viewFloating.view;
        if (view != null) {
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof FloatingLayout) {
                ZView zView;
                // 显示ZView
                if (view instanceof ZView) {
                    zView = (ZView) view;
                } else {
                    ViewGroup parentView = (ViewGroup) viewParent;
                    zView = new ZView(view.getContext());
                    parentView.removeView(view);
                    zView.addView(view);
                    parentView.addView(zView);
                }

                // 重置ZView宽高
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                if (params == null) {
                    params = new ViewGroup.MarginLayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else {
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
//                params.leftMargin = 0;
//                params.rightMargin = 0;
                zView.setLayoutParams(params);

                // 执行动画
                Scroller scroller = AnimFactory.getScroller(animFunStyle, viewFloating, new LinearInterpolator());
                zView.setScroller(scroller).setAnimListener(listener);

                View cView = ViewUtil.getRootView(view);
                int width = cView.getMeasuredWidth();
                if (width <= 0) {
                    // 设置测量模式
                    int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    cView.measure(measureSpec, measureSpec);
                    // 重新测量
                    width = cView.getMeasuredWidth();
                }
                int fromX = fLayerConfig.startX + fLayerConfig.width - 10;
                int toX = fLayerConfig.startX - width;

                zView.startScroll(-fromX, 0, fromX - toX, 0, fLayerConfig.animDuration);
            }
        }
    }
}
