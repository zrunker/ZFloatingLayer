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
 * @description: 计算子控件在X轴上的偏移量
 * 按照已经显示子控件在Y轴上偏移量来计算，来决定目标View需要显示的位置（X轴），从左到右
 * 该策略更为合理，但涉及到对View的大小获取，所以需要在所有子View测量完成之后在使用
 * PS：不支持补间动画，因为补间动画不能改变View的实质位置
 * @author: zoufengli01
 * @create: 2021-10-15 17:09
 **/
public class XByYOffset extends AbsStrategy {
    private final Random random = new Random();

    public XByYOffset(ViewGroup parentView) {
        super(parentView);
    }

    @Override
    public int[] getMargins(@NonNull View targetView,
                            @NonNull FLayerConfig fLayerConfig) {
        int[] margins = new int[4];
        int leftMargin = getLeftXByYOffset(targetView, fLayerConfig);
        if (leftMargin <= 0) {
            leftMargin = fLayerConfig.childHorMargin;
        }
        margins[0] = leftMargin;
        margins[1] = fLayerConfig.childVerMargin;
        margins[2] = fLayerConfig.childHorMargin;
        margins[3] = fLayerConfig.childVerMargin;
        return margins;
    }

    private int getLeftXByYOffset(@NonNull View targetView,
                                  @NonNull FLayerConfig fLayerConfig) {

        int measureWidth = targetView.getMeasuredWidth();
        if (measureWidth <= 0) {
            // 设置测量模式
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            targetView.measure(measureSpec, measureSpec);
            // 重新测量
            measureWidth = targetView.getMeasuredWidth();
        }
        int realW = measureWidth + fLayerConfig.childHorMargin * 2;

        if (parentView.getChildCount() > 0) {
            ArrayList<View> list = new ArrayList<>();
            for (int i = 0; i < parentView.getChildCount(); i++) {
                View view = parentView.getChildAt(i);
                if (view != targetView) {
                    list.add(view);
                }
            }
            Collections.sort(list, new Comparator<View>() {
                @Override
                public int compare(View o1, View o2) {
                    return (int) (o1.getX() - o2.getX());
                }
            });

            if (fLayerConfig.width <= 0) {
                fLayerConfig.width = parentView.getMeasuredWidth();
            }

            int lines = fLayerConfig.width / realW;
            ArrayList<String> lineList = new ArrayList<>();
            for (int i = 1; i <= lines; i++) {
                lineList.add(i + "");
            }

            for (View view : list) {
                float viewW = view.getMeasuredWidth() + fLayerConfig.childHorMargin * 2;
                float sX = view.getX() - fLayerConfig.childHorMargin;
                float eX = sX + viewW;

                int sLine = (int) (sX / realW + (sX % realW > 0 ? 1 : 0));
                int eLine = (int) (eX / realW + (eX % realW > 0 ? 1 : 0));

                if (lineList.size() > 0) {
                    lineList.remove(sLine + "");
                    lineList.remove(eLine + "");
                }
            }

            if (lineList.size() > 0) {
                String key = lineList.get(0);
                int value = (Integer.parseInt(key) - 1) * realW;
                int compWidth = fLayerConfig.width - realW;
                while (value > compWidth) {
                    value = value - realW;
                }
                return value;
            }

            int value = lines > 0 ? random.nextInt(lines) * realW : 0;
            int compWidth = fLayerConfig.width - realW;
            while (value > compWidth) {
                value = value - realW;
            }
            return value;
        }

        return fLayerConfig.childHorMargin;
    }
}
