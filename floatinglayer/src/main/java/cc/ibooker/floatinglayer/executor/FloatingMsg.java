package cc.ibooker.floatinglayer.executor;

import cc.ibooker.floatinglayer.cache.ViewFloating;
import cc.ibooker.floatinglayer.event.AnimFunStyle;
import cc.ibooker.floatinglayer.location.LocationStrategy;

/**
 * @program: ZFloatingLayer
 * @description: 事件消息包装类
 * @author: zoufengli01
 * @create: 2021-11-12 11:10
 **/
public class FloatingMsg {
    public ViewFloating vFloating;

    /*动画类型*/
    @AnimFunStyle
    public String animFunStyle;

    /*位置计算策略*/
    @LocationStrategy
    public int locationStrategy;

    /*动画运行时长*/
    public int animDuration;

    public FloatingMsg(ViewFloating vFloating, @AnimFunStyle String animFunStyle,
                       @LocationStrategy int locationStrategy, int animDuration) {
        this.vFloating = vFloating;
        this.animFunStyle = animFunStyle;
        this.locationStrategy = locationStrategy;
        this.animDuration = animDuration;
    }
}
