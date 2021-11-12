package cc.ibooker.floatinglayer.core.anim;

import android.view.View;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.core.flayer.FLayerConfig;


/**
 * @program: ZFloatingLayer
 * @description: 动画接口
 * @author: zoufengli01
 * @create: 2021-10-19 10:34
 **/
public interface IAnim {

    /**
     * 执行动画
     *
     * @param view         待执行View
     * @param fLayerConfig 容器属性
     * @param listener     动画回调监听
     */
    void executeAnim(@NonNull View view,
                     @NonNull FLayerConfig fLayerConfig,
                     @NonNull IAnimListener listener);
}
