package cc.ibooker.floatinglayer.location.strategy;

import androidx.annotation.NonNull;

import java.util.Set;

import cc.ibooker.floatinglayer.container.FloatingBean;
import cc.ibooker.floatinglayer.flayer.FLayerConfig;

/**
 * @program: ZFloatingLayer
 * @description: 位置策略接口
 * @author: zoufengli01
 * @create: 2021-10-15 17:12
 **/
public abstract class AbsStrategy {
    protected Set<FloatingBean> set;

    public AbsStrategy(Set<FloatingBean> set) {
        this.set = set;
    }

    /**
     * 获取Margin值
     *
     * @return 返回四个数据，左上右下
     */
    public abstract int[] getMargins(@NonNull FloatingBean targetEvent,
                                     @NonNull FLayerConfig fLayerConfig);
}
