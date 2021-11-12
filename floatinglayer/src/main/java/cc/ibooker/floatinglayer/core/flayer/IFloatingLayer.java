package cc.ibooker.floatinglayer.core.flayer;

import android.view.View;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.cache.ViewFloating;
import cc.ibooker.floatinglayer.core.flayer.view.strategy.LocationStrategy;

/**
 * @program: ZFloatingLayer
 * @description: 浮层对外接口类
 * @author: zoufengli01
 * @create: 2021-10-12 11:09
 **/
public interface IFloatingLayer extends IFloatingLayerConfig {

    /**
     * 展示
     */
    void show();

    /**
     * 隐藏
     */
    void hide();

    /**
     * 转化子控件
     *
     * @param view 待处理View
     */
    void resetChildViewLocation(@NonNull View view,
                                @LocationStrategy int locationStrategy);

    /**
     * 添加View
     *
     * @param view 待添加View
     */
    void addView(@NonNull View view);

    /**
     * 移除View
     *
     * @param vFloating 待处理View
     */
    void removeView(@NonNull ViewFloating vFloating);

    /**
     * 销毁
     */
    void destroy();

    /**
     * 清屏
     */
    void clear();

    /**
     * 获取属性
     */
    FLayerConfig fLayerConfig();
}
