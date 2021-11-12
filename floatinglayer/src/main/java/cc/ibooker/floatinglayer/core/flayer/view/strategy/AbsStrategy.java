package cc.ibooker.floatinglayer.core.flayer.view.strategy;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;


/**
 * @program: ZFloatingLayer
 * @description: 位置策略接口
 * @author: zoufengli01
 * @create: 2021-10-15 17:12
 **/
public abstract class AbsStrategy {
    protected ViewGroup parentView;

    public AbsStrategy(ViewGroup parentView) {
        this.parentView = parentView;
    }

    /**
     * 获取Margin值
     *
     * @return 返回四个数据，左上右下
     */
    public abstract int[] getMargins(@NonNull View targetView,
                                     @NonNull FLayerConfig fLayerConfig);
}
