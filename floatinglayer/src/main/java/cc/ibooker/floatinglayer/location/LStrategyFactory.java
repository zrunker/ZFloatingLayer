package cc.ibooker.floatinglayer.location;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Set;

import cc.ibooker.floatinglayer.container.FloatingBean;
import cc.ibooker.floatinglayer.location.strategy.AbsStrategy;
import cc.ibooker.floatinglayer.location.strategy.LocationRandom;
import cc.ibooker.floatinglayer.location.strategy.XByYOffset;
import cc.ibooker.floatinglayer.location.strategy.YByLine;
import cc.ibooker.floatinglayer.location.strategy.YByRandom;
import cc.ibooker.floatinglayer.location.strategy.YByXOffset;

/**
 * @program: ZFloatingLayer
 * @description: 位置策略工厂类
 * @author: zoufengli01
 * @create: 2021-10-18 22:33
 **/
public class LStrategyFactory {
    private static final HashMap<Integer, AbsStrategy> MAP = new HashMap<>();

    public static AbsStrategy getStrategy(@NonNull Set<FloatingBean> set,
                                          @LocationStrategy int locationStrategy) {
        AbsStrategy strategy = MAP.get(locationStrategy);
        if (strategy == null) {
            switch (locationStrategy) {
                case LocationStrategy.TOPY_LINE:
                    strategy = new YByLine(set);
                    break;
                case LocationStrategy.TOPY_X_OFFSET:
                    strategy = new YByXOffset(set);
                    break;
                case LocationStrategy.TOPY_RANDOM:
                    strategy = new YByRandom(set);
                    break;
                case LocationStrategy.LOCATION_RANDOM:
                    strategy = new LocationRandom(set);
                    break;
                case LocationStrategy.LEFTX_Y_OFFSET:
                    strategy = new XByYOffset(set);
                    break;
            }
        }
        if (strategy != null) {
            MAP.put(locationStrategy, strategy);
        }
        return strategy;
    }
}
