package cc.ibooker.floatinglayer.core.flayer;

import androidx.annotation.NonNull;

/**
 * @program: ZFloatingLayer
 * @description: 浮层属性接口
 * @author: zoufengli01
 * @create: 2021-10-15 10:30
 **/
public interface IFloatingLayerConfig {

    /**
     * 设置浮层位置
     *
     * @param sX X轴开始位置
     * @param sY Y轴开始位置
     */
    void location(int sX, int sY);

    /**
     * 设置浮层大小
     *
     * @param w 宽
     * @param h 高
     */
    void size(int w, int h);

    /**
     * 设置显示区域
     */
    void region(@NonNull @FLayerRegion String region);

    /**
     * 动画延迟时间
     *
     * @param animDuration 单位ms
     */
    void animDuration(int animDuration);

    /**
     * 子View垂直偏移量
     *
     * @param childVerMargin 偏移量 px
     */
    void childVerMargin(int childVerMargin);

    /**
     * 子View水平偏移量
     *
     * @param childHorMargin 偏移量 px
     */
    void childHorMargin(int childHorMargin);

}
