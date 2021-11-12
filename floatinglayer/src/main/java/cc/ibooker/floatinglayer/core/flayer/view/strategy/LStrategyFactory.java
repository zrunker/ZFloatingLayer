package cc.ibooker.floatinglayer.core.flayer.view.strategy;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.HashMap;

/**
 * @program: ZFloatingLayer
 * @description: 位置策略工厂类
 * @author: zoufengli01
 * @create: 2021-10-18 22:33
 **/
public class LStrategyFactory {
    private static final HashMap<Integer, AbsStrategy> MAP = new HashMap<>();

    public static AbsStrategy getStrategy(@NonNull ViewGroup viewGroup,
                                          @LocationStrategy int locationStrategy) {
        AbsStrategy strategy = MAP.get(locationStrategy);
        if (strategy == null) {
            switch (locationStrategy) {
                case LocationStrategy.TOPY_LINE:
                    strategy = new YByLine(viewGroup);
                    break;
                case LocationStrategy.TOPY_X_OFFSET:
                    strategy = new YByXOffset(viewGroup);
                    break;
                case LocationStrategy.TOPY_RANDOM:
                    strategy = new YByRandom(viewGroup);
                    break;
                case LocationStrategy.LOCATION_RANDOM:
                    strategy = new LocationRandom(viewGroup);
                    break;
                case LocationStrategy.LEFTX_Y_OFFSET:
                    strategy = new XByYOffset(viewGroup);
                    break;
            }
        }
        if (strategy != null) {
            MAP.put(locationStrategy, strategy);
        }
        return strategy;
    }
}
