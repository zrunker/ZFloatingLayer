package cc.ibooker.floatinglayer.flayer;

import androidx.annotation.NonNull;

import cc.ibooker.floatinglayer.executor.FloatingMsg;

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
     * 添加浮层消息
     *
     * @param fMsg 待添加数据
     */
    void addFloatingMsg(@NonNull FloatingMsg fMsg);

    /**
     * 移除浮层消息
     *
     * @param fMsg 待添加数据
     */
    void removeFloatingMsg(@NonNull FloatingMsg fMsg);

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
