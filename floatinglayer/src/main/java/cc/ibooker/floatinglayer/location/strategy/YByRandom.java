package cc.ibooker.floatinglayer.location.strategy;

import androidx.annotation.NonNull;

import java.util.Random;
import java.util.Set;

import cc.ibooker.floatinglayer.container.FloatingBean;
import cc.ibooker.floatinglayer.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 计算子控件在Y轴上的偏移量，适用于从右向左动画
 * 随机显示
 * @author: zoufengli01
 * @create: 2021-10-15 18:08
 **/
public class YByRandom extends AbsStrategy {
    private final Random random = new Random();

    public YByRandom(Set<FloatingBean> set) {
        super(set);
    }

    @Override
    public int[] getMargins(@NonNull FloatingBean targetEvent,
                            @NonNull FLayerConfig fLayerConfig) {
        int[] margins = new int[4];
        margins[0] = fLayerConfig.childHorMargin;
        int topMargin = getTopYByRandom(targetEvent, fLayerConfig);
        if (topMargin <= 0) {
            topMargin = fLayerConfig.childVerMargin;
        }
        margins[1] = topMargin;
        margins[2] = fLayerConfig.childHorMargin;
        margins[3] = fLayerConfig.childVerMargin;
        return margins;
    }

    private int getTopYByRandom(@NonNull FloatingBean targetEvent,
                                @NonNull FLayerConfig fLayerConfig) {
        int topY = 0;

        int measuredHeight = targetEvent.height;

        int lines = 3;
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
