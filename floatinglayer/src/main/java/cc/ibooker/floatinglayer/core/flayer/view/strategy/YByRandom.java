package cc.ibooker.floatinglayer.core.flayer.view.strategy;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import java.util.Random;

import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 计算子控件在Y轴上的偏移量，适用于从右向左动画
 * 随机显示
 * @author: zoufengli01
 * @create: 2021-10-15 18:08
 **/
public class YByRandom extends AbsStrategy {
    private final Random random = new Random();
    private final int defaultLines = 3;

    public YByRandom(ViewGroup parentView) {
        super(parentView);
    }

    @Override
    public int[] getMargins(@NonNull View targetView, @NonNull FLayerConfig fLayerConfig) {
        int[] margins = new int[4];
        margins[0] = fLayerConfig.childHorMargin;
        int topMargin = getTopYByRandom(targetView, fLayerConfig);
        if (topMargin <= 0) {
            topMargin = fLayerConfig.childVerMargin;
        }
        margins[1] = topMargin;
        margins[2] = fLayerConfig.childHorMargin;
        margins[3] = fLayerConfig.childVerMargin;
        return margins;
    }

    private int getTopYByRandom(@NonNull View targetView, @NonNull FLayerConfig fLayerConfig) {
        int topY = 0;

        int measuredHeight = targetView.getMeasuredHeight();
        if (measuredHeight <= 0) {
            // 设置测量模式
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            targetView.measure(measureSpec, measureSpec);
            // 重新测量
            measuredHeight = targetView.getMeasuredHeight();
        }

        if (fLayerConfig.height <= 0) {
            fLayerConfig.height = parentView.getMeasuredHeight();
        }

        int lines = defaultLines;
        if (fLayerConfig.height > 0 && measuredHeight > 0) {
            lines = fLayerConfig.height / measuredHeight;
        }

        if (lines > 1) {
            int realH = measuredHeight + fLayerConfig.childVerMargin * 2;

            int value = random.nextInt(lines - 1);

            topY = value * realH;
            int compHeight = fLayerConfig.height - realH;
            while (topY > compHeight) {
                topY = topY - realH;
            }
        }

        return topY;
    }
}
