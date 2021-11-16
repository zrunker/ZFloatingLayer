package cc.ibooker.floatinglayer.event;

import cc.ibooker.floatinglayer.combination.CombinationEnum;
import cc.ibooker.floatinglayer.util.Config;

/**
 * @program: ZFloatingLayer
 * @description: 事件包装类
 * @author: zoufengli01
 * @create: 2021-11-12 10:45
 **/
public class FloatingEvent<T> {
    public IEvent<T> iEvent;

    /*动画组合类型*/
    public CombinationEnum combinationEnum = Config.combinationEnum;

    /*动画运行时长，默认4000ms*/
    public int animDuration = Config.defaultAnimDuration;

    public FloatingEvent(IEvent<T> iEvent) {
        this.iEvent = iEvent;
    }

    public FloatingEvent(IEvent<T> iEvent, CombinationEnum combinationEnum) {
        this.iEvent = iEvent;
        this.combinationEnum = combinationEnum;
    }

    public FloatingEvent(IEvent<T> iEvent, int animDuration) {
        this.iEvent = iEvent;
        this.animDuration = animDuration;
    }

    public FloatingEvent(IEvent<T> iEvent, CombinationEnum combinationEnum, int animDuration) {
        this.iEvent = iEvent;
        this.combinationEnum = combinationEnum;
        this.animDuration = animDuration;
    }
}
