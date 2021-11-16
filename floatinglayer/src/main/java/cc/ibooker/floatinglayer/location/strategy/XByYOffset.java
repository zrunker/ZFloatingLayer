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
 * @description: 计算子控件在X轴上的偏移量
 * 按照已经显示子控件在Y轴上偏移量来计算，来决定目标View需要显示的位置（X轴），从左到右
 * 该策略更为合理，但涉及到对View的大小获取，所以需要在所有子View测量完成之后在使用
 * @author: zoufengli01
 * @create: 2021-10-15 17:09
 **/
public class XByYOffset extends AbsStrategy {
    private final Random random = new Random();

    public XByYOffset(Set<FloatingBean> set) {
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
        margins[1] = fLayerConfig.childVerMargin;
        margins[2] = fLayerConfig.childHorMargin;
        margins[3] = fLayerConfig.childVerMargin;
        return margins;
    }

    private int getLeftXByYOffset(@NonNull FloatingBean targetEvent,
                                  @NonNull FLayerConfig fLayerConfig) {

        int realW = targetEvent.width + fLayerConfig.childHorMargin * 2;

        if (set.size() > 0) {
            ArrayList<FloatingBean> list = new ArrayList<>();
            for (FloatingBean fEvent : set) {
                if (fEvent != targetEvent) {
                    list.add(fEvent);
                }
            }

            try {
                Collections.sort(list, new Comparator<FloatingBean>() {
                    @Override
                    public int compare(FloatingBean o1, FloatingBean o2) {
                        return o1.fromX > o2.fromX ? 1 : -1;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            int lines = fLayerConfig.width / realW;
            ArrayList<String> lineList = new ArrayList<>();
            for (int i = 1; i <= lines; i++) {
                lineList.add(i + "");
            }

            for (FloatingBean item : list) {
                float viewW = item.width + fLayerConfig.childHorMargin * 2;
                float sX = item.fromX - fLayerConfig.childHorMargin;
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
