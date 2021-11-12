package cc.ibooker.floatinglayer.core.event;


import cc.ibooker.floatinglayer.core.anim.IAnim;
import cc.ibooker.floatinglayer.core.combination.CombinationEnum;

/**
 * @program: ZFloatingLayer
 * @description: 浮层事件
 * @author: zoufengli01
 * @create: 2021-10-12 11:26
 **/
public class FloatingEvent<T> {

    /*状态，是否就绪*/
    public boolean isReady = false;

    /*动画组合*/
    public CombinationEnum combinationEnum;

    /*自定义动画*/
    public IAnim anim;

    /*事件*/
    public IEvent<T> event;

    private FloatingEvent() {
    }

    public FloatingEvent(IEvent<T> event) {
        this.event = event;
    }

    public FloatingEvent(IEvent<T> event, IAnim anim) {
        this.event = event;
        this.anim = anim;
    }

    public FloatingEvent(IEvent<T> event, CombinationEnum combinationEnum) {
        this.event = event;
        this.combinationEnum = combinationEnum;
    }

    public String getAnimType() {
        if (combinationEnum != null) {
            return combinationEnum.getAnimType();
        }
        return null;
    }

    @Override
    public String toString() {
        return "FloatingEvent{" +
                "combinationEnum=" + combinationEnum +
                ", anim=" + anim +
                ", event=" + event +
                '}';
    }
}
