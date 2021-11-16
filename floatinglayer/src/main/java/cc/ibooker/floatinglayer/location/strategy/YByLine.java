package cc.ibooker.floatinglayer.location.strategy;

import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import cc.ibooker.floatinglayer.container.FloatingBean;
import cc.ibooker.floatinglayer.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 计算子控件在Y轴上的偏移量，适用于从右向左动画
 * 优先显示最近未有子控件显示的行位置（Y轴）
 * @author: zoufengli01
 * @create: 2021-10-15 17:29
 **/
public class YByLine extends AbsStrategy {
    private final Queue<Integer> queue = new ArrayDeque<>();
    private final ArrayList<Integer> lineList = new ArrayList<>();
    private final Random random = new Random();

    public YByLine(Set<FloatingBean> set) {
        super(set);
    }

    @Override
    public int[] getMargins(@NonNull FloatingBean targetEvent,
                            @NonNull FLayerConfig fLayerConfig) {
        int[] margins = new int[4];
        margins[0] = fLayerConfig.childHorMargin;
        int topMargin = getTopYByLine(targetEvent, fLayerConfig);
        if (topMargin <= 0) {
            topMargin = fLayerConfig.childVerMargin;
        }
        margins[1] = topMargin;
        margins[2] = fLayerConfig.childHorMargin;
        margins[3] = fLayerConfig.childVerMargin;
        return margins;
    }

    private int getTopYByLine(@NonNull FloatingBean targetEvent,
                              @NonNull FLayerConfig fLayerConfig) {
        int topY = 0;

        int measuredHeight = targetEvent.height;

        int lines = 3;
        if (fLayerConfig.height > 0 && measuredHeight > 0) {
            lines = fLayerConfig.height / measuredHeight;
        }

        if (lines > 1) {
            int cLines = lines - 1;
            if (lineList.size() != cLines
                    || queue.size() != lineList.size()) {

                lineList.clear();
                for (int i = 0; i < cLines; i++) {
                    lineList.add(i);
                }
                Collections.shuffle(lineList);

                queue.clear();
                for (Integer i : lineList) {
                    if (!queue.contains(i)) {
                        queue.add(i);
                    }
                }
            }

            int realH = measuredHeight + fLayerConfig.childVerMargin * 2;

            Integer value = queue.poll();
            queue.add(value);
            if (value == null) {
                value = random.nextInt(lines - 1);
            }

            topY = value * realH;
            int compHeight = fLayerConfig.height - realH;
            while (topY > compHeight) {
                topY = topY - realH;
            }
        }
        return topY;
    }
}
