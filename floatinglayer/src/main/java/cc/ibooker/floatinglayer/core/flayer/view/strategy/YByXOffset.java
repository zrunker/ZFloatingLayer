package cc.ibooker.floatinglayer.core.flayer.view.strategy;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 计算子控件在Y轴上的偏移量，适用于从右向左动画
 * 按照已经显示子控件在X轴上偏移量来计算，来决定目标View需要显示的位置（Y轴），从上向下
 * 该策略更为合理，但涉及到对View的大小获取，所以需要在所有子View测量完成之后在使用
 * PS：不支持补间动画，因为补间动画不能改变View的实质位置
 * @author: zoufengli01
 * @create: 2021-10-15 17:09
 **/
public class YByXOffset extends AbsStrategy {
    private final Random random = new Random();

    public YByXOffset(ViewGroup parentView) {
        super(parentView);
    }

    @Override
    public int[] getMargins(@NonNull View targetView,
                            @NonNull FLayerConfig fLayerConfig) {
        int[] margins = new int[4];
        margins[0] = fLayerConfig.childHorMargin;
        int topMargin = getTopYByXOffset(targetView, fLayerConfig);
        if (topMargin <= 0) {
            topMargin = fLayerConfig.childVerMargin;
        }
        margins[1] = topMargin;
        margins[2] = fLayerConfig.childHorMargin;
        margins[3] = fLayerConfig.childVerMargin;
        return margins;
    }

    private int getTopYByXOffset(@NonNull View targetView,
                                 @NonNull FLayerConfig fLayerConfig) {
        int measureHeight = targetView.getMeasuredHeight();
        if (measureHeight <= 0) {
            // 设置测量模式
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            targetView.measure(measureSpec, measureSpec);
            // 重新测量
            measureHeight = targetView.getMeasuredHeight();
        }
        int realH = measureHeight + fLayerConfig.childVerMargin * 2;

        if (parentView.getChildCount() > 0) {
            ArrayList<View> list = new ArrayList<>();
            for (int i = 0; i < parentView.getChildCount(); i++) {
                View view = parentView.getChildAt(i);
                if (view != targetView) {
                    list.add(view);
                }
            }

            ArrayList<View> temp = new ArrayList<>();
            for (View view : list) {
                float x = view.getX() + view.getMeasuredWidth() + fLayerConfig.childHorMargin;
                if (x >= parentView.getMeasuredWidth()) {
                    temp.add(view);
                }
            }
            Collections.sort(temp, new Comparator<View>() {
                @Override
                public int compare(View o1, View o2) {
                    return (int) (o1.getX() - o2.getX());
                }
            });

            if (fLayerConfig.height <= 0) {
                fLayerConfig.height = parentView.getMeasuredHeight();
            }

            int lines = fLayerConfig.height / realH;
            ArrayList<String> lineList = new ArrayList<>();
            for (int i = 1; i <= lines; i++) {
                lineList.add(i + "");
            }

            for (View view : temp) {
                float viewH = view.getMeasuredHeight() + fLayerConfig.childVerMargin * 2;
                float sY = view.getY() - fLayerConfig.childVerMargin;
                float eY = sY + viewH;

                int sLine = (int) (sY / realH + (sY % realH > 0 ? 1 : 0));
                int eLine = (int) (eY / realH + (eY % realH > 0 ? 1 : 0));

                if (lineList.size() > 0) {
                    lineList.remove(sLine + "");
                    lineList.remove(eLine + "");
                }
            }

            if (lineList.size() > 0) {
                String key = lineList.get(0);
                int value = (Integer.parseInt(key) - 1) * realH;

                int compHeight = fLayerConfig.height - realH;
                while (value > compHeight) {
                    value = value - realH;
                }
                return value;
            }

            int value = lines > 0 ? random.nextInt(lines) * realH : 0;
            int compHeight = fLayerConfig.height - realH;
            while (value > compHeight) {
                value = value - realH;
            }
            return value;
        }

        return fLayerConfig.childVerMargin;
    }
}
