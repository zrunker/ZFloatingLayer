package cc.ibooker.floatinglayer.core.flayer.view;


import static cc.ibooker.floatinglayer.core.util.ConstantUtil.MAX_CONCURRENT_NUM;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;
import cc.ibooker.floatinglayer.core.flayer.view.strategy.AbsStrategy;
import cc.ibooker.floatinglayer.core.flayer.view.strategy.LStrategyFactory;
import cc.ibooker.floatinglayer.core.flayer.view.strategy.LocationStrategy;

/**
 * @program: ZFloatingLayer
 * @description: 浮层布局
 * @author: zoufengli01
 * @create: 2021-10-12 10:40
 **/
public class FloatingLayout extends FrameLayout {
    private ExecutorService iOExecutor;

    private ExecutorService iOExecutor() {
        if (iOExecutor == null) {
            int num = MAX_CONCURRENT_NUM > 0 ? MAX_CONCURRENT_NUM * 2 : 100;
            iOExecutor = new ThreadPoolExecutor(num, num, 20,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100));
        }
        return iOExecutor;
    }

    public FloatingLayout(@NonNull Context context) {
        this(context, null);
    }

    public FloatingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 初始化子控件位置
     *
     * @param child 子控件
     */
    public void resetChildViewLocation(@NonNull final View child,
                                       @NonNull final FLayerConfig fLayerConfig,
                                       @LocationStrategy final int locationStrategy) {
        iOExecutor().execute(new Runnable() {
            @Override
            public void run() {
                AbsStrategy strategy = LStrategyFactory
                        .getStrategy(FloatingLayout.this, locationStrategy);
                if (strategy != null) {
                    int[] margins = strategy.getMargins(child, fLayerConfig);
                    MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                    params.leftMargin = margins[0];
                    params.topMargin = margins[1];
                    params.rightMargin = margins[2];
                    params.bottomMargin = margins[3];
//                    child.setLayoutParams(params);
                    child.postInvalidate();
                }
            }
        });
    }

}

