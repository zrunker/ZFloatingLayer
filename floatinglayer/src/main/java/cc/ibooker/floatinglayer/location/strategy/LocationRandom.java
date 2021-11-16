package cc.ibooker.floatinglayer.location.strategy;

import androidx.annotation.NonNull;

import java.util.Random;
import java.util.Set;

import cc.ibooker.floatinglayer.container.FloatingBean;
import cc.ibooker.floatinglayer.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 计算子控件在X轴上的偏移量 - 随机值
 * @author: zoufengli01
 * @create: 2021-10-15 17:09
 **/
public class LocationRandom extends AbsStrategy {
    private final Random random = new Random();
    private final int defaultLines = 3;

    public LocationRandom(Set<FloatingBean> set) {
        super(set);
    }

    @Override
    public int[] getMargins(@NonNull FloatingBean targetEvent,
                            @NonNull FLayerConfig fLayerConfig) {
        int[] margins = new int[4];
        int leftMargin = getLeftXByYOffset(targetEvent, fLayerConfig);
        if (leftMargin <= 0) {
            leftMargin = fLayerConfig.childHorMargin;
        }
        margins[0] = leftMargin;

        int topMargin = getTopYByRandom(targetEvent, fLayerConfig);
        if (topMargin <= 0) {
            topMargin = fLayerConfig.childVerMargin;
        }
        margins[1] = topMargin;

        margins[2] = fLayerConfig.childHorMargin;
        margins[3] = fLayerConfig.childVerMargin;
        return margins;
    }

    private int getLeftXByYOffset(@NonNull FloatingBean targetEvent,
                                  @NonNull FLayerConfig fLayerConfig) {
        int leftX = 0;

        int measuredWidth = targetEvent.width;

        int lines = defaultLines;
        if (fLayerConfig.width > 0 && measuredWidth > 0) {
            lines = fLayerConfig.width / measuredWidth;
        }
        if (lines > 1) {
            int realW = measuredWidth + fLayerConfig.childHorMargin * 2;

            int value = random.nextInt(lines);

            leftX = value * realW;
            int compWidth = fLayerConfig.width - realW;
            while (leftX > compWidth) {
                leftX = leftX - realW;
            }
        }

        return leftX;
    }

    private int getTopYByRandom(@NonNull FloatingBean targetEvent,
                                @NonNull FLayerConfig fLayerConfig) {
        int topY = 0;

        int measuredHeight = targetEvent.height;

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
