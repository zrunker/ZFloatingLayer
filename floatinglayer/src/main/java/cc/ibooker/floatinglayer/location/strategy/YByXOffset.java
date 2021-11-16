package cc.ibooker.floatinglayer.location.strategy;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Set;

import cc.ibooker.floatinglayer.container.FloatingBean;
import cc.ibooker.floatinglayer.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 计算子控件在Y轴上的偏移量，适用于从右向左动画
 * 按照已经显示子控件在X轴上偏移量来计算，来决定目标View需要显示的位置（Y轴），从上向下
 * 该策略更为合理，但涉及到对View的大小获取，所以需要在所有子View测量完成之后在使用
 * @author: zoufengli01
 * @create: 2021-10-15 17:09
 **/
public class YByXOffset extends AbsStrategy {
    private final Random random = new Random();

    public YByXOffset(Set<FloatingBean> set) {
        super(set);
    }

    @Override
    public int[] getMargins(@NonNull FloatingBean targetEvent,
                            @NonNull FLayerConfig fLayerConfig) {
        int[] margins = new int[4];
        margins[0] = fLayerConfig.startX + fLayerConfig.width + fLayerConfig.childHorMargin;
        int topMargin = getTopYByXOffset(targetEvent, fLayerConfig);
        if (topMargin <= 0) {
            topMargin = fLayerConfig.childVerMargin;
        }
        margins[1] = topMargin;
        margins[2] = fLayerConfig.childHorMargin;
        margins[3] = fLayerConfig.childVerMargin;
        return margins;
    }

    private int getTopYByXOffset(@NonNull FloatingBean targetEvent,
                                 @NonNull FLayerConfig fLayerConfig) {
        int realH = targetEvent.height + fLayerConfig.childVerMargin * 2;

        if (set.size() > 0) {
            ArrayList<FloatingBean> list = new ArrayList<>();
            for (FloatingBean fEvent : set) {
                if (fEvent != targetEvent) {
                    list.add(fEvent);
                }
            }

            ArrayList<FloatingBean> temp = new ArrayList<>();
            for (FloatingBean item : list) {
                float eX = item.fromX + item.width + fLayerConfig.childHorMargin;
                if (eX >= targetEvent.fromX) {
                    temp.add(item);
                }
            }

            try {
                Collections.sort(temp, new Comparator<FloatingBean>() {
                    @Override
                    public int compare(FloatingBean o1, FloatingBean o2) {
                        return o1.fromX > o2.fromX ? 1 : -1;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            int lines = fLayerConfig.height / realH;
            ArrayList<String> lineList = new ArrayList<>();
            for (int i = 1; i <= lines; i++) {
                lineList.add(i + "");
            }

            for (FloatingBean item : temp) {
                float viewH = item.height + fLayerConfig.childVerMargin * 2;
                float sY = item.topY - fLayerConfig.childVerMargin;
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
